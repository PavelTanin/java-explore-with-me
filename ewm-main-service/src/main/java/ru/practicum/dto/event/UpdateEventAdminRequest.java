package ru.practicum.dto.event;

import lombok.Data;
import ru.practicum.enums.StateAction;
import ru.practicum.model.location.Location;

import javax.validation.constraints.Size;

@Data
public class UpdateEventAdminRequest extends UpdateEventUserRequest {

    @Size(min = 20, max = 2000, message = "Аннотация события имеет некорректную длинну")
    private String annotation;

    private Long category;

    @Size(min = 20, max = 7000, message = "Описание события имеет некоректную длинну")
    private String description;

    private String eventDate;

    private Location location;

    private Boolean path;

    private Boolean paid;

    private Long participantLimit;

    private Boolean requestModeration;

    private StateAction stateAction;

    @Size(min = 3, max = 120, message = "Некорректная длинна названия")
    private String title;
}
