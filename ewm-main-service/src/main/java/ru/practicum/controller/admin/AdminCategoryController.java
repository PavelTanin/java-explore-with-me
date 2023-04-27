package ru.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.service.category.CategoryService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity addCategory(@RequestBody @Valid NewCategoryDto newCategoryDto) {
        log.info("От администратора получен POST-запрос на добавление новой категории: {}", newCategoryDto.toString());
        return new ResponseEntity<>(categoryService.addCategory(newCategoryDto), HttpStatus.CREATED);
    }

    @PatchMapping("/{catId}")
    public ResponseEntity updateCategory(@RequestBody @Valid CategoryDto categoryDto,
                                         @Positive @PathVariable Long catId) {
        log.info("От администратора получен PATCH-запрос на изменение категории c id: {}", catId);
        return new ResponseEntity(categoryService.updateCategory(categoryDto, catId), HttpStatus.OK);
    }

    @DeleteMapping("/{catId}")
    public ResponseEntity deleteCategory(@Positive @PathVariable Long catId) {
        log.info("От администратора получен DELETE-запрос на удалениче категории с id: {}", catId);
        return new ResponseEntity(new String[]{categoryService.deleteCategory(catId)}, HttpStatus.NO_CONTENT);
    }


}
