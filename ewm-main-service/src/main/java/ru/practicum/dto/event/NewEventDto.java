package ru.practicum.dto.event;


import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import ru.practicum.model.location.Location;

import javax.validation.constraints.*;

@AllArgsConstructor
@Setter
@Getter
@ToString
public class NewEventDto {

    @NotEmpty(message = "Аннотация события не может быть пустым")
    @Size(min = 20, max = 2000, message = "Аннотация события имеет некорректную длинну")
    private String annotation;

    private Long category;

    @NotEmpty(message = "Описание события не может быть пустым")
    @Size(min = 20, max = 7000, message = "Описание события имеет некоректную длинну")
    private String description;

    @NotEmpty(message = "Для создания события необходимо указать дату его проведения")
    private String eventDate;

    @NotNull(message = "Необходимо указать координаты проведения события")
    private Location location;

    @NotNull(message = "Информация о необходимости оплачивать участие не указана")
    private Boolean paid;

    @Value("0")
    private Long participantLimit;

    @Value("true")
    private Boolean requestModeration;

    @NotEmpty(message = "У события должно быть название")
    @Size(min = 3, max = 120, message = "Некорректная длинна названия")
    private String title;
}
