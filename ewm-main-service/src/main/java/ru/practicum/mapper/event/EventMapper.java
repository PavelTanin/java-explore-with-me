package ru.practicum.mapper.event;

import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.enums.RequestStatus;
import ru.practicum.mapper.category.CategoryMapper;
import ru.practicum.mapper.user.UserMapper;
import ru.practicum.model.event.Event;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EventMapper {

    private static final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Event toEvent(NewEventDto newEventDto) {
        return new Event(newEventDto.getTitle(), newEventDto.getAnnotation(), newEventDto.getDescription(),
                LocalDateTime.parse(newEventDto.getEventDate(), timeFormat), newEventDto.getPaid(), newEventDto.getParticipantLimit(),
                newEventDto.getRequestModeration(), newEventDto.getLocation());
    }

    public static EventFullDto toEventFullDto(Event event) {
        EventFullDto eventFullDto = new EventFullDto(event.getAnnotation(), CategoryMapper.toCategoryDto(event.getCategory()), event.getCreatedOn(),
                event.getDescription(), event.getEventDate(), event.getId(), UserMapper.toUserShortDto(event.getInitiator()),
                event.getLocation(), event.getPaid(), event.getParticipantLimit(), event.getPublishedOn(), event.getTitle(),
                event.getRequestModeration(), event.getViews());
        if (event.getState() != null) {
            eventFullDto.setState(event.getState());
        }
        if (event.getRequests() != null) {
            eventFullDto.setConfirmedRequests(event.getRequests().stream()
                    .filter(o -> o.getStatus().equals(RequestStatus.CONFIRMED))
                    .count());
        }
        return eventFullDto;
    }

    public static EventShortDto toEventShortDto(Event event) {
        return new EventShortDto(event.getId(), event.getTitle(), event.getAnnotation(),
                CategoryMapper.toCategoryDto(event.getCategory()), event.getPaid(), event.getEventDate(),
                UserMapper.toUserShortDto(event.getInitiator()));
    }
}
