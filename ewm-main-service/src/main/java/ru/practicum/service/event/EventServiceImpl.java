package ru.practicum.service.event;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.client.StatsClient;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.dto.event.*;
import ru.practicum.enums.RequestStatus;
import ru.practicum.enums.State;
import ru.practicum.exception.EventPubProblemException;
import ru.practicum.exception.ObjectNotFoundException;
import ru.practicum.exception.UserWrongPropertiesException;
import ru.practicum.exception.WrongEventTimeException;
import ru.practicum.mapper.event.EventMapper;
import ru.practicum.model.event.Event;
import ru.practicum.model.user.User;
import ru.practicum.repository.category.CategoryRepository;
import ru.practicum.repository.event.EventRepository;
import ru.practicum.repository.location.LocationRepository;
import ru.practicum.repository.user.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final EventRepository eventRepository;

    private final UserRepository userRepository;

    private final CategoryRepository categoryRepository;

    private final LocationRepository locationRepository;

    private final StatsClient statsClient;

    private static final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Transactional
    public EventFullDto addEvent(NewEventDto newEventDto, Long userId) {
        log.info("Добавляется новое событие {} от пользователя id: {}", newEventDto.toString(), userId);
        if (LocalDateTime.parse(newEventDto.getEventDate(), timeFormat).isBefore(LocalDateTime.now().plusHours(2))) {
            log.info("Некорректно указано время проведения события");
            throw new WrongEventTimeException("Дата проведения " + newEventDto.getEventDate() +
                    " должна наступить не раньше чем через 2 часа");
        }
        Event event = EventMapper.toEvent(newEventDto);
        event.setInitiator(userRepository.findById(userId).orElseThrow(() -> new ObjectNotFoundException("Пользователь не зарегестрирован")));
        event.setCreatedOn(LocalDateTime.now());
        event.setCategory(categoryRepository.findById(newEventDto.getCategory()).get());
        event.setState(State.PENDING);
        locationRepository.save(newEventDto.getLocation());
        log.info("Добавлено новое событие");
        return EventMapper.toEventFullDto(eventRepository.save(event));

    }

    @Transactional
    public EventFullDto updateEventByUser(UpdateEventUserRequest updateEvent, Long userId, Long eventId) {
        log.info("Обновляется информация о событии id: {}", eventId);
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new ObjectNotFoundException("Событие еще создано"));
        if (event.getState().equals(State.PUBLISHED)) {
            log.info("Событие уже опубликована");
            throw new EventPubProblemException("Невозможно внести изменения в опубликованное событие");
        }
        updateEvent(updateEvent, eventId, event);
        log.info("Информация о событии id: {} обновлена", eventId);
        return EventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Transactional
    public EventFullDto updateEventByAdmin(UpdateEventAdminRequest updateEvent, Long eventId) {
        log.info("Обновляется информация о событии id: {}", eventId);
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new ObjectNotFoundException("Событие еще создано"));
        if (event.getState().equals(State.PUBLISHED) || event.getState().equals(State.CANCELED)) {
            log.info("Редактируемое событие уже опубликовано");
            throw new EventPubProblemException("Событие уже опубликовано");
        }
        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
            log.info("Время события меньше чем через час");
            throw new WrongEventTimeException("Опубликовать событие можно не позже чем за час до начала");
        }
        updateEvent(updateEvent, eventId, event);
        log.info("Информация о событии id: {} обновлена", eventId);
        return EventMapper.toEventFullDto(eventRepository.save(event));
    }

    public List<EventFullDto> getEventsByAdmin(List<Long> usersIds, List<State> states, List<Long> cats,
                                               String start, String end, Integer from, Integer size) {
        log.info("Получение списка всех событий");
        Pageable pageable = PageRequest.of(from, size);
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        if (start != null) {
            startTime = LocalDateTime.parse(start, timeFormat);
        }
        if (end != null) {
            endTime = LocalDateTime.parse(end, timeFormat);
        }
        Page<Event> events = eventRepository.getEventsByAdmin(usersIds, states, cats, startTime,
                endTime, pageable);
        log.info("Получен список всех событий");
        return events.stream().map(EventMapper::toEventFullDto).collect(Collectors.toList());
    }

    @Override
    public EventFullDto getUserEvent(Long userId, Long eventId) {
        log.info("Получение информации о событии id: {} пользователя id: {}", eventId, userId);
        User user = userRepository.findById(userId).orElseThrow(()
                -> new ObjectNotFoundException("Пользователь не зарегестрирован"));
        Event event = eventRepository.findById(eventId).orElseThrow(()
                -> new ObjectNotFoundException("Такого события нет"));
        if (!event.getInitiator().equals(user)) {
            log.info("Пользователь не является инициатором события");
            throw new UserWrongPropertiesException("Полную информацию о событии может посмотреть только инициатор");
        }
        return EventMapper.toEventFullDto(event);
    }

    public List<EventFullDto> getAllUserEvents(Long userId, Integer from, Integer size) {
        log.info("Получение информации обо всех событиях пользователя id: {}", userId);
        User user = userRepository.findById(userId).orElseThrow(()
                -> new ObjectNotFoundException("Пользователь не зарегестрирован"));
        Pageable pageable = PageRequest.of(from, size);
        log.info("Получена информация обо всех собтиях пользователя");
        return eventRepository.findAllByInitiatorOrderByIdAsc(user, pageable).stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    public EventFullDto getEvent(Long eventId) {
        log.info("Получение информации о событии id: {}", eventId);
        Event event = eventRepository.findById(eventId).orElseThrow(()
                -> new ObjectNotFoundException("Такого события не существует"));
        if (!event.getState().equals(State.PUBLISHED)) {
            log.info("Информаци о событии еще не опубликовано");
            throw new EventPubProblemException("Событие еще не прошло модерацию");
        }
        List<Event> eventInList = List.of(event);
        getViews(eventInList);
        log.info("Информация о событии получена");
        return EventMapper.toEventFullDto(eventInList.stream().findFirst().get());
    }

    @Override
    public List<EventFullDto> getEvents(String text, List<Long> cats, Boolean paid, String start, String end,
                                        Boolean available, String sort, Integer from, Integer size) {
        log.info("Получение списка событий");
        Pageable pageable = PageRequest.of(from, size);
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        if (start != null) {
            startTime = LocalDateTime.parse(start, timeFormat);
        }
        if (end != null) {
            endTime = LocalDateTime.parse(end, timeFormat);
        }
        List<Event> events = eventRepository.getEvents(text.toLowerCase(), cats, paid, startTime,
                endTime, pageable);
        getViews(events);
        if (available != null && available.equals(true)) {
            events.stream().filter(o -> o.getRequests().stream()
                    .filter(r -> r.getStatus().equals(RequestStatus.CONFIRMED)).count() < o.getParticipantLimit())
                    .collect(Collectors.toList());
        }
        if (sort != null) {
            switch (sort) {
                case "EVENT_DATE":
                    events.sort((Comparator.comparing(Event::getEventDate)));
                    break;
                case "VIEWS":
                    events.sort(Comparator.comparing(Event::getViews));
                    break;
            }

        }
        return events.stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    private void getViews(List<Event> events) {
        String startTime = (events.stream().min(Comparator.comparing(Event::getPublishedOn))
                .get()
                .getPublishedOn()).format(timeFormat);
        String endTime = LocalDateTime.now().format(timeFormat);
        List<String> ids = events.stream()
                .map(o -> "/events/" + o.getId())
                .collect(Collectors.toList());
        Map<Long, Long> allViews = statsClient.getStats(ids, startTime, endTime).stream()
                .collect(Collectors.toMap(o -> Long.parseLong(List.of(o.getUri().split("/")).get(2)), ViewStatsDto::getHits));
        for (Event event : events) {
            event.setViews(allViews.get(event.getId()));
        }
    }

    private void updateEvent(UpdateEventUserRequest updateEvent, Long eventId, Event event) {
        if (updateEvent.getAnnotation() != null) {
            log.info("Для события id: {} обновлена аннотация", eventId);
            event.setAnnotation(updateEvent.getAnnotation());
        }
        if (updateEvent.getCategory() != null) {
            log.info("Для события id: {} обновлена категория {} -> {}", eventId, event.getCategory().getId(),
                    updateEvent.getCategory());
            event.setCategory(categoryRepository.findById(updateEvent.getCategory()).get());
        }
        if (updateEvent.getDescription() != null) {
            log.info("Для события id: {} обновлено описание", eventId);
            event.setDescription(updateEvent.getDescription());
        }
        if (updateEvent.getEventDate() != null) {
            if (LocalDateTime.parse(updateEvent.getEventDate(), timeFormat).isBefore(LocalDateTime.now().plusHours(2))) {
                log.info("Некорректно указано время проведения события");
                throw new WrongEventTimeException("Дата проведения " + updateEvent.getEventDate() +
                        " должна наступить не раньше чем через 2 часа");
            }
            log.info("Для события id: {} обновлено время проведения", eventId);
            event.setEventDate(LocalDateTime.parse(updateEvent.getEventDate(), timeFormat));
        }
        if (updateEvent.getLocation() != null) {
            log.info("Для события id: {} обновлено место проведения", eventId);
            event.setLocation(locationRepository.save(updateEvent.getLocation()));
        }
        if (updateEvent.getPaid() != null) {
            log.info("Для события id: {} обновлена информация о необходимости оплачивать участие", eventId);
            event.setPaid(updateEvent.getPaid());
        }
        if (updateEvent.getParticipantLimit() != null) {
            log.info("Для события id: {} обновлено число доступных мест", eventId);
            event.setParticipantLimit(updateEvent.getParticipantLimit());
        }
        if (updateEvent.getRequestModeration() != null) {
            log.info("Для события id: {} обновлена необходимость одобрения заявок на участие", eventId);
            event.setRequestModeration(updateEvent.getRequestModeration());
        }
        if (updateEvent.getStateAction() != null) {
            switch (updateEvent.getStateAction()) {
                case SEND_TO_REVIEW:
                    event.setState(State.PENDING);
                    break;
                case CANCEL_REVIEW:
                case REJECT_EVENT:
                    event.setState(State.CANCELED);
                    break;
                default:
                    event.setState(State.PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
            }
            log.info("Для события id: {} обновлен статус рассмотрения", eventId);
        }
        if (updateEvent.getTitle() != null) {
            log.info("Для события id: {} обновлено название", eventId);
            event.setTitle(updateEvent.getTitle());
        }
    }
}
