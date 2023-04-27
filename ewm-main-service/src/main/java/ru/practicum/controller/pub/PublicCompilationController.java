package ru.practicum.controller.pub;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.service.compilation.CompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
public class PublicCompilationController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final CompilationService compilationService;

    @GetMapping
    public ResponseEntity getCompilations(@RequestParam(name = "pinned", required = false) Boolean pinned,
                                          @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                          @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Получен GET-запрос на получение списка всех подборок");
        return new ResponseEntity(compilationService.getCompilations(pinned, from, size), HttpStatus.OK);
    }

    @GetMapping("/{compId}")
    public ResponseEntity getCompilation(@Positive @PathVariable(name = "compId") Long compId) {
        log.info("Получен GET-запрос на получение информации о подборке id: {}", compId);
        return new ResponseEntity(compilationService.getCompilation(compId), HttpStatus.OK);
    }
}
