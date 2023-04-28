package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.service.StatsService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatsController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final StatsService statsService;

    @PostMapping("/hit")
    public ResponseEntity<String> addHit(@RequestBody EndpointHitDto endpointHitDto) {
        log.info("Получен POST-запрос от прирожения {}", endpointHitDto.getApp());
        return new ResponseEntity<>(statsService.addHit(endpointHitDto), HttpStatus.CREATED);
    }

    @GetMapping("/stats")
    public ResponseEntity<List<ViewStatsDto>> getStats(@RequestParam(name = "start") String start,
                                                       @RequestParam(name = "end") String end,
                                                       @RequestParam(required = false, name = "uris") List<String> uris,
                                                       @RequestParam(name = "unique", defaultValue = "false") String unique) {
        log.info("Получен GET-запрос с параметрами start:{}, end:{}, uris:{}, unique:{}", start, end, uris, unique);
        return new ResponseEntity<>(statsService.getStats(start, end, uris, unique), HttpStatus.OK);

    }

}
