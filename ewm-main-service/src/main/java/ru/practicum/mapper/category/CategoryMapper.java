package ru.practicum.mapper.category;

import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.model.category.Category;

public class CategoryMapper {

    public static Category toCategory(NewCategoryDto newCategoryDto) {
        return new Category(newCategoryDto.getName());
    }

    public static CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(category.getId(), category.getName());
    }
}
