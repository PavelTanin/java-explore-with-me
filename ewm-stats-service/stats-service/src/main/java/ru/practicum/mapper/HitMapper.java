package ru.practicum.mapper;

import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HitMapper {

    private static DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static EndpointHit toEndpointHit(EndpointHitDto endpointHitDto) {
        return new EndpointHit(endpointHitDto.getApp(), endpointHitDto.getUri(),
                endpointHitDto.getIp(), LocalDateTime.parse(endpointHitDto.getTimestamp(), TIME_FORMAT));
    }

    public static ViewStatsDto toViewStatsDto(ViewStats viewStats) {
        return new ViewStatsDto(viewStats.getApp(), viewStats.getUri(), viewStats.getHits());
    }
}
