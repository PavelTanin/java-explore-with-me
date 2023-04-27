package ru.practicum.controller.pub;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.client.StatsClient;
import ru.practicum.service.event.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class PublicEventController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final EventService eventService;

    private final StatsClient statsClient;

    @GetMapping("/{eventId}")
    public ResponseEntity getEvent(@Positive @PathVariable(name = "eventId") Long eventId,
                                   HttpServletRequest request) throws JsonProcessingException {
        log.info("Получен GET-запрос на получение информации о событии id: {}", eventId);
        log.info(statsClient.hit(request, "ewm-main-service"));
        return new ResponseEntity(eventService.getEvent(eventId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getEvents(@RequestParam(name = "text", required = false) String text,
                                    @RequestParam(name = "categories", required = false) List<Long> cats,
                                    @RequestParam(name = "paid", required = false) Boolean paid,
                                    @RequestParam(name = "rangeStart", required = false) String start,
                                    @RequestParam(name = "rangeEnd", required = false) String end,
                                    @RequestParam(name = "onlyAvailable", required = false) Boolean available,
                                    @RequestParam(name = "sort", required = false) String sort,
                                    @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                    @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Получен GET-запрос на получение списка событий");
        return new ResponseEntity(eventService.getEvents(text, cats, paid, start, end, available,
                sort, from, size), HttpStatus.OK);
    }
}
