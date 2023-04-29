package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.user.UserShortDto;
import ru.practicum.enums.State;
import ru.practicum.model.location.Location;

import java.time.LocalDateTime;

@Data
public class EventDto {

    private Long id;

    private String title;

    private String annotation;

    private CategoryDto category;

    private Boolean paid;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private UserShortDto initiator;

    private String description;

    private Long participantLimit;

    private State state;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;

    private Location location;

    private Boolean requestModeration;

    public EventDto(Long id, String title, String annotation, CategoryDto category,
                    Boolean paid, LocalDateTime eventDate, UserShortDto initiator,
                    String description, Long participantLimit,
                    LocalDateTime createdOn, Location location, Boolean requestModeration) {
        this.id = id;
        this.title = title;
        this.annotation = annotation;
        this.category = category;
        this.paid = paid;
        this.eventDate = eventDate;
        this.initiator = initiator;
        this.description = description;
        this.participantLimit = participantLimit;
        this.createdOn = createdOn;
        this.location = location;
        this.requestModeration = requestModeration;
    }
}
