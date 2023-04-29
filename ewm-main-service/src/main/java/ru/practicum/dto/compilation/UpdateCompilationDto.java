package ru.practicum.dto.compilation;

import lombok.Data;

import java.util.List;

@Data
public class UpdateCompilationDto {

    private List<Long> events;

    private Boolean pinned;

    private String title;


}
