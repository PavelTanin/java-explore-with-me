package ru.practicum.service.category;

import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.NewCategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto addCategory(NewCategoryDto newCategoryDto);

    CategoryDto updateCategory(CategoryDto categoryDto, Long catId);

    String deleteCategory(Long catId);

    CategoryDto getCategory(Long catId);

    List<CategoryDto> getCategories(Integer from, Integer size);
}
