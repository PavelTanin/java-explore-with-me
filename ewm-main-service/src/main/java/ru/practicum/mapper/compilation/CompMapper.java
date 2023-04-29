package ru.practicum.mapper.compilation;

import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.mapper.event.EventMapper;
import ru.practicum.model.compilation.Compilation;

import java.util.stream.Collectors;

public class CompMapper {

    public static Compilation toCompilation(NewCompilationDto newCompilationDto) {
        return new Compilation(newCompilationDto.getTitle(), newCompilationDto.getPinned());
    }

    public static CompilationDto toCompilationDto(Compilation compilation) {
        return new CompilationDto(compilation.getId(), compilation.getTitle(),
                compilation.getPinned(), compilation.getEvents().stream()
                .map(EventMapper::toEventShortDto).collect(Collectors.toList()));
    }
}
