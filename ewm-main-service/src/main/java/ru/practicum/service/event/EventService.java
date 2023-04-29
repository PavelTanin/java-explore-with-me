package ru.practicum.service.event;

import ru.practicum.dto.event.*;
import ru.practicum.enums.State;

import java.util.List;

public interface EventService {

    EventFullDto addEvent(NewEventDto newEventDto, Long userId);

    EventFullDto updateEventByUser(UpdateEventUserRequest updateEvent, Long userId, Long eventId);

    EventFullDto updateEventByAdmin(UpdateEventAdminRequest updateEvent, Long eventId);

    List<EventFullDto> getEventsByAdmin(List<Long> usersIds, List<State> states, List<Long> cats, String start,
                                        String end, Integer from, Integer size);

    EventFullDto getUserEvent(Long userId, Long eventId);

    List<EventFullDto> getAllUserEvents(Long userId, Integer from, Integer size);

    EventFullDto getEvent(Long eventId);

    List<EventFullDto> getEvents(String text, List<Long> cats, Boolean paid, String start,
                                 String end, Boolean available, String sort, Integer from, Integer size);

}
