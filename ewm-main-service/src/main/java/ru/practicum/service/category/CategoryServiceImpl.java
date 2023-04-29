package ru.practicum.service.category;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.exception.ObjectNotFoundException;
import ru.practicum.mapper.category.CategoryMapper;
import ru.practicum.model.category.Category;
import ru.practicum.repository.category.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final CategoryRepository categoryRepository;

    @Transactional
    public CategoryDto addCategory(NewCategoryDto newCategoryDto) {
        log.info("Добавляется новая категория: {}", newCategoryDto.toString());
        Category category = CategoryMapper.toCategory(newCategoryDto);
        log.info("Категория добавлена");
        return CategoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Transactional
    public CategoryDto updateCategory(CategoryDto categoryDto, Long catId) {
        log.info("Изменяется категория с id: {}", catId);
        Category category = categoryRepository.findById(catId).get();
        if (category == null) {
            log.info("Категория с таким id еще не создана");
            throw new ObjectNotFoundException("Категория не найдена");
        }
        category.setName(categoryDto.getName());
        return CategoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Transactional
    public String deleteCategory(Long catId) {
        log.info("Удаление категории с id: {}", catId);
        if (!categoryRepository.existsById(catId)) {
            log.info("Категория с таким идентификатором не найдена");
            throw new ObjectNotFoundException("Категории с таким идентификатором не существует");
        }
        categoryRepository.deleteById(catId);
        log.info("Категория удалена");
        return "Категория удалена";
    }

    public CategoryDto getCategory(Long catId) {
        log.info("Получение информации о категории id: {}", catId);
        log.info("Получена информации о категории");
        return CategoryMapper.toCategoryDto(categoryRepository.findById(catId).orElseThrow(()
        -> new ObjectNotFoundException("Такая категория еще не создана")));
    }

    public List<CategoryDto> getCategories(Integer from, Integer size) {
        log.info("Получение информации о всех категориях");
        Pageable pageable = PageRequest.of(from, size);
        return categoryRepository.findAll(pageable).stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }
}
