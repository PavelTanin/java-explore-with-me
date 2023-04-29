package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.user.UserShortDto;
import ru.practicum.enums.State;
import ru.practicum.model.location.Location;

import java.time.LocalDateTime;

@Data
public class EventFullDto {

    private String annotation;

    private CategoryDto category;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long confirmedRequests;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;

    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private Long id;

    private UserShortDto initiator;

    private Location location;

    private Boolean paid;

    private Long participantLimit;

    private Boolean requestModeration;

    private LocalDateTime publishedOn;

    private State state;

    private String title;

    private Long views;

    public EventFullDto(String annotation, CategoryDto category,
                        LocalDateTime createdOn, String description, LocalDateTime eventDate,
                        Long id, UserShortDto initiator, Location location,
                        Boolean paid, Long participantLimit,
                        LocalDateTime publishedOn, String title, Boolean requestModeration,
                        Long views) {
        this.annotation = annotation;
        this.category = category;
        this.createdOn = createdOn;
        this.description = description;
        this.eventDate = eventDate;
        this.id = id;
        this.initiator = initiator;
        this.location = location;
        this.paid = paid;
        this.participantLimit = participantLimit;
        this.publishedOn = publishedOn;
        this.title = title;
        this.requestModeration = requestModeration;
        this.views = views;
    }
}
