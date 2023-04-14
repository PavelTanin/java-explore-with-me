package ru.practicum.service;

import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;

import java.util.List;

public interface StatsService {

    String addHit(EndpointHitDto endpointHitDto);

    List<ViewStatsDto> getStats(String start, String end, List<String> uris, String unique);
}
