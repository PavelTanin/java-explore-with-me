package ru.practicum.service.request;

import ru.practicum.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.dto.request.ParticipationRequestDto;

import java.util.List;

public interface RequestService {

    ParticipationRequestDto addRequest(Long userId, Long eventId);

    ParticipationRequestDto deleteRequest(Long userId, Long requestId);

    List<ParticipationRequestDto> getAllUserRequests(Long userId);

    List<ParticipationRequestDto> getUserEventRequests(Long userId, Long eventId);

    List<EventRequestStatusUpdateResult> updateUserEventRequests(EventRequestStatusUpdateRequest update,
                                                                 Long userId, Long eventId);
}
