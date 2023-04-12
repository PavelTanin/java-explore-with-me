package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.mapper.HitMapper;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;
import ru.practicum.repository.StatsRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final StatsRepository statsRepository;

    private final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public String addHit(EndpointHitDto endpointHitDto) {
        log.info("Добавляется информация о посещении: {}", endpointHitDto.toString());
        EndpointHit endpointHit = HitMapper.toEndpointHit(endpointHitDto);
        statsRepository.save(endpointHit);
        log.info("Клик добавлен!");
        return "Информация сохранена";

    }

    @Override
    public List<ViewStatsDto> getStats(String start, String end, List<String> uris, String unique) {
        log.info("Попытка получить статистику посещений");
        switch (unique) {
            case "true":
                if (uris == null) {
                    log.info("Получена статистика посещения всех ссылок уникальными " +
                            "пользователями в период с {} до {}", start, end);
                    return statsRepository.findAllHitsTimestampBetweenAndUnique(LocalDateTime.parse(start, TIME_FORMAT),
                                    LocalDateTime.parse(end, TIME_FORMAT)).stream()
                            .sorted(comparing(ViewStats::getHits).reversed())
                            .map(HitMapper::toViewStatsDto)
                            .collect(Collectors.toList());
                } else {
                    log.info("Получена статистика посещения ссылок({}) уникальными " +
                            "пользователями в период с {} до {}", uris, start, end);
                    return statsRepository.findAllHitsTimestampBetweenAndUriInAndUnique(LocalDateTime.parse(start, TIME_FORMAT),
                                    LocalDateTime.parse(end, TIME_FORMAT), uris).stream()
                            .sorted(comparing(ViewStats::getHits).reversed())
                            .map(HitMapper::toViewStatsDto)
                            .collect(Collectors.toList());
                }
            default:
                if (uris == null) {
                    log.info("Получена статистика посещения всех ссылок в период с {} до {}", start, end);
                    return statsRepository
                            .findAllHitsTimestampBetweenAndNotUnique(LocalDateTime.parse(start, TIME_FORMAT),
                                    LocalDateTime.parse(end, TIME_FORMAT)).stream()
                            .sorted(comparing(ViewStats::getHits).reversed())
                            .map(HitMapper::toViewStatsDto)
                            .collect(Collectors.toList());
                } else {
                    log.info("Получена статистика посещения ссылок({}) в период с {} до {}", uris, start, end);
                    return statsRepository
                            .findAllHitsTimestampBetweenAndUriInAndNotUnique(LocalDateTime.parse(start, TIME_FORMAT),
                            LocalDateTime.parse(end, TIME_FORMAT), uris).stream()
                            .sorted(comparing(ViewStats::getHits).reversed())
                            .map(HitMapper::toViewStatsDto)
                            .collect(Collectors.toList());
                }
        }
    }
}
