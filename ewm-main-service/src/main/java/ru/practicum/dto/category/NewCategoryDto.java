package ru.practicum.dto.category;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class NewCategoryDto {

    @NotEmpty(message = "Поле name не может быть пустым")
    private String name;
}
