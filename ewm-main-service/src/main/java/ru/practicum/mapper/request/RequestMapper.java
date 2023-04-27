package ru.practicum.mapper.request;

import ru.practicum.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.model.request.Request;

import java.time.format.DateTimeFormatter;

public class RequestMapper {

    private static final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static ParticipationRequestDto toRequestDto(Request request) {
        return new ParticipationRequestDto(request.getCreated().format(timeFormat), request.getEvent().getId(),
                request.getId(), request.getRequester().getId(), request.getStatus());
    }

    public static EventRequestStatusUpdateResult toRequestResult(Request request) {
        return new EventRequestStatusUpdateResult(request.getCreated().format(timeFormat), request.getEvent().getId(),
                request.getId(), request.getRequester().getId(), request.getStatus());
    }
}
