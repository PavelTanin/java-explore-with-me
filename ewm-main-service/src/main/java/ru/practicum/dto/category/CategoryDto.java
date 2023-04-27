package ru.practicum.dto.category;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
public class CategoryDto {

    private Long id;

    @NotEmpty(message = "Поле name не может быть пустым")
    private String name;

    public CategoryDto(String name) {
        this.name = name;
    }
}
