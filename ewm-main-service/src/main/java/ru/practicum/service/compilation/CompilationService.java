package ru.practicum.service.compilation;

import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.dto.compilation.UpdateCompilationDto;

import java.util.List;

public interface CompilationService {

    CompilationDto addCompilation(NewCompilationDto newCompilationDto);

    CompilationDto updateCompilation(UpdateCompilationDto updateCompilationDto, Long compId);

    String deleteCompilation(Long compId);

    CompilationDto getCompilation(Long compId);

    List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size);
}
