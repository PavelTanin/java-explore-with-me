package ru.practicum.mapper;

import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;

public class HitMapper {


    public static EndpointHit toEndpointHit(EndpointHitDto endpointHitDto) {
        return new EndpointHit(endpointHitDto.getApp(), endpointHitDto.getUri(),
                endpointHitDto.getIp(), endpointHitDto.getTimestamp());
    }

    public static ViewStatsDto toViewStatsDto(ViewStats viewStats) {
        return new ViewStatsDto(viewStats.getApp(), viewStats.getUri(), viewStats.getHits());
    }
}
