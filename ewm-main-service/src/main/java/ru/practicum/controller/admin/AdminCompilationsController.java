package ru.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.dto.compilation.UpdateCompilationDto;
import ru.practicum.service.compilation.CompilationService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
public class AdminCompilationsController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final CompilationService compilationService;

    @PostMapping
    public ResponseEntity addCompilation(@RequestBody @Valid NewCompilationDto newCompilationDto) {
        log.info("Получен POST-запрос от администратора на создание новой подборки: {}", newCompilationDto.toString());
        return new ResponseEntity(compilationService.addCompilation(newCompilationDto), HttpStatus.CREATED);
    }

    @PatchMapping("/{compId}")
    public ResponseEntity updateCompilation(@RequestBody UpdateCompilationDto updateCompilationDto,
                                             @Positive @PathVariable(name = "compId") Long compId) {
        log.info("Получен PATCH-запрос от администратора на обновление подборки id: {}", compId);
        return new ResponseEntity(compilationService.updateCompilation(updateCompilationDto, compId), HttpStatus.OK);
    }

    @DeleteMapping("/{compId}")
    public ResponseEntity deleteCompilation(@Positive @PathVariable(name = "compId") Long compId) {
        log.info("Получен DELETE-запрос от администратора на удаление подборки id: {}", compId);
        return new ResponseEntity(new String[]{compilationService.deleteCompilation(compId)}, HttpStatus.NO_CONTENT);
    }
}
