package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.user.UserShortDto;

import java.time.LocalDateTime;

@Data
public class EventShortDto {

        private Long id;

    private String title;

    private String annotation;

    private CategoryDto category;

    private Boolean paid;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private UserShortDto initiator;

    private Long confirmedRequests;

    private Long views;

    public EventShortDto(Long id, String title, String annotation, CategoryDto category,
                         Boolean paid, LocalDateTime eventDate, UserShortDto initiator) {
        this.id = id;
        this.title = title;
        this.annotation = annotation;
        this.category = category;
        this.paid = paid;
        this.eventDate = eventDate;
        this.initiator = initiator;
    }
}
