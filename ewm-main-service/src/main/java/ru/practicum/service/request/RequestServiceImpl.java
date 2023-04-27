package ru.practicum.service.request;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.enums.RequestStatus;
import ru.practicum.enums.State;
import ru.practicum.exception.*;
import ru.practicum.mapper.request.RequestMapper;
import ru.practicum.model.event.Event;
import ru.practicum.model.request.Request;
import ru.practicum.model.user.User;
import ru.practicum.repository.event.EventRepository;
import ru.practicum.repository.request.RequestRepository;
import ru.practicum.repository.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final RequestRepository requestRepository;

    private final EventRepository eventRepository;

    private final UserRepository userRepository;

    @Transactional
    public ParticipationRequestDto addRequest(Long userId, Long eventId) {
        log.info("Добавляется заявка на участие пользователя id: {} в событии id: {}", userId, eventId);
        User user = userRepository.findById(userId).orElseThrow(()
                -> new ObjectNotFoundException("Пользователь не зарегестрирован"));
        Event event = eventRepository.findById(eventId).orElseThrow(()
                -> new ObjectNotFoundException("Событие еще не создано"));
        requestCheck(userId, eventId, event, event.getRequests(), event.getParticipantLimit());
        Request request = new Request(user, event, LocalDateTime.now());
        if (event.getRequestModeration().equals(true)) {
            request.setStatus(RequestStatus.PENDING);
        } else {
            request.setStatus(RequestStatus.CONFIRMED);
        }
        log.info("Заявка создана");
        return RequestMapper.toRequestDto(requestRepository.save(request));
    }

    @Transactional
    public ParticipationRequestDto deleteRequest(Long userId, Long requestId) {
        log.info("Пользователь id: {} отменяет свою заявку id: {}", userId, requestId);
        Request request = requestRepository.findById(requestId).orElseThrow(()
                -> new ObjectNotFoundException("Такой заявки не существует"));
        if (!request.getRequester().getId().equals(userId)) {
            log.info("Пользователь отменяет не свою заявку");
            throw new UserWrongPropertiesException("Отменить заявку на участие может только автор заявки");
        }
        request.setStatus(RequestStatus.CANCELED);
        log.info("Заявка на участие отменена");
        return RequestMapper.toRequestDto(requestRepository.save(request));
    }

    public List<ParticipationRequestDto> getAllUserRequests(Long userId) {
        log.info("Получение списка заявок пользователя id: {}", userId);
        User user = userRepository.findById(userId).orElseThrow(()
                -> new ObjectNotFoundException("Пользователь не зарегестрирован"));
        return requestRepository.findAllByRequesterOrderByIdAsc(user).stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    public List<ParticipationRequestDto> getUserEventRequests(Long userId, Long eventId) {
        log.info("Получение списка заявок на участие в событии id: {}", eventId);
        User user = userRepository.findById(userId).orElseThrow(()
                -> new ObjectNotFoundException("Пользователь не зарегестрирован"));
        Event event = eventRepository.findById(eventId).orElseThrow(()
                -> new ObjectNotFoundException("Событие еще не создано"));
        if (!event.getInitiator().equals(user)) {
            log.info("Пользователь не является инициатором события");
            throw new UserWrongPropertiesException("Заявки на участие в событии могут просматривать только инициаторы");
        }
        return requestRepository.findAllByEventOrderByIdAsc(event).stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<EventRequestStatusUpdateResult> updateUserEventRequests(EventRequestStatusUpdateRequest update,
                                                                        Long userId, Long eventId) {
        log.info("Обновление статуса заявок для события id: {}", eventId);
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new ObjectNotFoundException("Такого события нет"));
        List<Request> requests = requestRepository.findAllByIdInOrderById(update.getRequestIds());
        if (update.getStatus().equals(RequestStatus.REJECTED)) {
            if (requests.stream().filter(o -> o.getStatus().equals(RequestStatus.CONFIRMED)).findAny() != null) {
                log.info("Попытка отменить уже принятую заявку");
                throw new CancelingRequestException("Нельзя отменять уже принятые заявки");
            }
            requests.stream().forEach(o -> o.setStatus(RequestStatus.REJECTED));
            log.info("Заявки ids: {} на участие в событии id: {} отменены", update.getRequestIds().toString(), eventId);
            return requestRepository.saveAll(requests).stream()
                    .map(RequestMapper::toRequestResult)
                    .collect(Collectors.toList());
        } else {
            Long requestLimit = event.getParticipantLimit();
            Long approvedRequests = event.getRequests().stream().filter(o -> o.getStatus().equals(RequestStatus.CONFIRMED))
                    .count();
            int updateLimit = Math.toIntExact(requestLimit - approvedRequests);
            if (updateLimit <= 0 || event.getRequestModeration().equals(false)) {
                log.info("Достигнут лимит одобренных заявок или модерация заявок не требуется");
                throw new RequestLimitException("Лимит участников события достигнут или заявки не требуют подтверждения");
            }

            if (updateLimit >= requests.size()) {
                requests.stream().forEach(o -> o.setStatus(RequestStatus.CONFIRMED));
                log.info("Заявки ids: {} на участие в событии id: {} приняты",
                        update.getRequestIds().toString(), eventId);
                return requestRepository.saveAll(requests).stream()
                        .map(RequestMapper::toRequestResult)
                        .collect(Collectors.toList());
            } else {
                for (int i = 0; i <= updateLimit; i++) {
                    requests.get(i).setStatus(RequestStatus.CONFIRMED);
                }
                log.info("Заявки ids: {} на участие в событии id: {} частично приняты",
                        update.getRequestIds().toString(), eventId);
                requests.stream().filter(o -> !o.getStatus().equals(RequestStatus.CONFIRMED))
                        .forEach(o -> o.setStatus(RequestStatus.REJECTED));
            }
            log.info("Статусы заявок обновлены");
            return requestRepository.saveAll(requests).stream()
                    .map(RequestMapper::toRequestResult)
                    .collect(Collectors.toList());
        }
    }

    private void requestCheck(Long userId, Long eventId, Event event, List<Request> requests, Long participantLimit) {
        if (event.getInitiator().getId().equals(userId)) {
            log.info("Заявку отправил инициатор события");
            throw new UserWrongPropertiesException("Инициаторы события не могут подавать заявку на участие");
        }
        if (!event.getState().equals(State.PUBLISHED)) {
            log.info("Событие не опубликовано");
            throw new EventPubProblemException("Нельзя подать заявку на участие в неопубликованном событии");
        }
        if (!(requests.stream().filter(o -> o.getStatus().equals(RequestStatus.CONFIRMED)).count() < participantLimit)) {
            log.info("Лимит заявок достигнут");
            throw new RequestLimitException("Для этого события достигнут лимит заявок");
        }
        if (!(requestRepository.findByRequesterIdAndEventId(userId, eventId) == null)) {
            log.info("Пользователь уже оставил заявку на участие в этом событии");
            throw new RequestLimitException("Пользователь уже оставлял заявку на участие в этом событии");
        }
    }
}
