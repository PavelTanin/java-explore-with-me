package ru.practicum.controller.priv;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.event.UpdateEventUserRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.service.event.EventService;
import ru.practicum.service.request.RequestService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
public class PrivateEventController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final EventService eventService;

    private final RequestService requestService;

    @PostMapping
    public ResponseEntity<EventFullDto> addEvent(@RequestBody @Valid NewEventDto newEventDto,
                                                 @Positive @PathVariable Long userId) {
        log.info("Получен POST-запрос от пользователя id: {} на публикуцаю нового события: {}", userId, newEventDto.toString());
        return new ResponseEntity<>(eventService.addEvent(newEventDto, userId), HttpStatus.CREATED);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> updateEvent(@RequestBody @Valid UpdateEventUserRequest updateEvent,
                                      @Positive @PathVariable(name = "userId") Long userId,
                                      @Positive @PathVariable(name = "eventId") Long eventId) {
        log.info("Получен PATCH-запрос от пользователя id: {} на изменение события id: {}", userId, eventId);
        return new ResponseEntity<>(eventService.updateEventByUser(updateEvent, userId, eventId), HttpStatus.OK);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventFullDto> getUserEvent(@Positive @PathVariable(name = "userId") Long userId,
                                       @Positive @PathVariable(name = "eventId") Long eventId) {
        log.info("Получен GET-запрос от пользователя id: {} на просмотр события id: {}", userId, eventId);
        return new ResponseEntity<>(eventService.getUserEvent(userId, eventId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<EventFullDto>> getAllUserEvents(@Positive @PathVariable(name = "userId") Long userId,
                                                               @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                               @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Получен GET-запрос от пользователя id: {} на получение списка созданных им событий", userId);
        return new ResponseEntity<>(eventService.getAllUserEvents(userId, from, size), HttpStatus.OK);
    }

    @GetMapping("/{eventId}/requests")
    public ResponseEntity<List<ParticipationRequestDto>> getUserEventRequests(@Positive @PathVariable(name = "userId") Long userId,
                                                                              @Positive @PathVariable(name = "eventId") Long eventId) {
        log.info("Получен GET-запрос от пользователя id: {} на просмотр заявок на участие в событии id: {}", userId, eventId);
        return new ResponseEntity<>(requestService.getUserEventRequests(userId, eventId), HttpStatus.OK);
    }

    @PatchMapping("/{eventId}/requests")
    public ResponseEntity<EventRequestStatusUpdateResult> updateUserEventRequests(@RequestBody @Valid EventRequestStatusUpdateRequest updates,
                                                                                 @Positive @PathVariable(name = "userId") Long userId,
                                                                                 @Positive @PathVariable(name = "eventId") Long eventId) {
        log.info("Получен PATCH-запрос от пользователя id: {} на подтверждение заявок " +
                "на участие в его событии id: {}", userId, eventId);
        return new ResponseEntity<>(requestService.updateUserEventRequests(updates, userId, eventId), HttpStatus.OK);
    }

}
