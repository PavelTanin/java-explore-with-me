package ru.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.UpdateEventAdminRequest;
import ru.practicum.enums.State;
import ru.practicum.service.event.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class AdminEventController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final EventService eventService;

    @PatchMapping("/{eventId}")
    public ResponseEntity updateEvent(@RequestBody @Valid UpdateEventAdminRequest updateEvent,
                                      @Positive @PathVariable(name = "eventId") Long eventId) {
        log.info("Получен PATCH-запрос от администратора на изменение события id: {}",eventId);
        return new ResponseEntity(eventService.updateEventByAdmin(updateEvent, eventId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getEventsByAdmin(@Positive @RequestParam(name = "users", required = false) List<Long> usersIds,
                                    @RequestParam(name = "states", required = false) List<State> states,
                                    @RequestParam(name = "categories", required = false) List<Long> cats,
                                    @RequestParam(name = "rangeStart", required = false) String start,
                                    @RequestParam(name = "rangeEnd", required = false) String end,
                                    @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                    @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Получен GET-запрос от администратора на получение списка всех событий");
        return new ResponseEntity(eventService.getEventsByAdmin(usersIds, states, cats, start,
                end, from, size), HttpStatus.OK);
    }
}
