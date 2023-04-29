package ru.practicum.service.compilation;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.dto.compilation.UpdateCompilationDto;
import ru.practicum.exception.ObjectNotFoundException;
import ru.practicum.mapper.compilation.CompMapper;
import ru.practicum.model.compilation.Compilation;
import ru.practicum.repository.compilation.CompilationRepository;
import ru.practicum.repository.event.EventRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final CompilationRepository compilationRepository;

    private final EventRepository eventRepository;

    @Transactional
    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        log.info("Создается новая подборка событий");
        Compilation compilation = CompMapper.toCompilation(newCompilationDto);
        compilation.setEvents(eventRepository.findAllById(newCompilationDto.getEvents()));
        log.info("Создана новая подборка - {}", compilation.getTitle());
        return CompMapper.toCompilationDto(compilationRepository.save(compilation));
    }

    @Transactional
    public CompilationDto updateCompilation(UpdateCompilationDto updateCompilationDto, Long compId) {
        log.info("Изменение подборки id: {}", compId);
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(()
                -> new ObjectNotFoundException("Такой подборки событий не существует"));
        if (updateCompilationDto.getTitle() != null) {
            log.info("Название события изменено");
            compilation.setTitle(updateCompilationDto.getTitle());
        }
        if (updateCompilationDto.getPinned() != null) {
            log.info("Значение pinned изменено");
            compilation.setPinned(updateCompilationDto.getPinned());
        }
        if (updateCompilationDto != null) {
            log.info("Перечень событий, входящих в подборку изменен");
            compilation.setEvents(eventRepository.findAllById(updateCompilationDto.getEvents()));
        }
        log.info("Подборка событий изменена");
        return CompMapper.toCompilationDto(compilationRepository.save(compilation));
    }

    @Transactional
    public String deleteCompilation(Long compId) {
        log.info("Удаляем подборку");
        if (!compilationRepository.existsById(compId)) {
            log.info("Такой подборки не существует");
            throw new ObjectNotFoundException("Такой подборки событий не существует");
        }
        compilationRepository.deleteById(compId);
        log.info("Подборка удалена");
        return "Подборка удалена";
    }

    public CompilationDto getCompilation(Long compId) {
        log.info("Получение информации о подборке id: {}", compId);
        log.info("Информаци о подборке получена");
        return CompMapper.toCompilationDto(compilationRepository.findById(compId).orElseThrow(()
        -> new ObjectNotFoundException("Такой подборки не существует")));
    }

    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        log.info("Получение списка подборок");
        Pageable pageable = PageRequest.of(from, size);
        Page<Compilation> compilations;
        if (pinned != null) {
            log.info("Сформирован список подборок, с параметром pinned = {}", pinned);
            compilations = compilationRepository.findAllByPinnedOrderByIdAsc(pinned, pageable);
        } else {
            log.info("Сформирован список всех подборок");
            compilations = compilationRepository.findAll(pageable);
        }
        log.info("Получен список подборок");
        return compilations.stream().map(CompMapper::toCompilationDto).collect(Collectors.toList());
    }
}
