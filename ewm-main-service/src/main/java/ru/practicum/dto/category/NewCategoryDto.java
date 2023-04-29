package ru.practicum.dto.category;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class NewCategoryDto {

    @NotEmpty(message = "Поле name не может быть пустым")
    private String name;
}
