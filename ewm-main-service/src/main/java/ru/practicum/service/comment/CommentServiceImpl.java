package ru.practicum.service.comment;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.exception.ObjectNotFoundException;
import ru.practicum.exception.UserWrongPropertiesException;
import ru.practicum.mapper.comment.CommentMapper;
import ru.practicum.model.comment.Comment;
import ru.practicum.model.event.Event;
import ru.practicum.model.user.User;
import ru.practicum.repository.comment.CommentRepository;
import ru.practicum.repository.event.EventRepository;
import ru.practicum.repository.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final EventRepository eventRepository;

    private final UserRepository userRepository;

    private final CommentRepository commentRepository;

    @Transactional
    public CommentDto addComment(CommentDto commentDto, Long userId, Long eventId) {
        log.info("Добавляется новый комментарий");
        User user = userRepository.findById(userId).orElseThrow(()
                -> new ObjectNotFoundException("Пользователь не зарегестрирован"));
        Event event = eventRepository.findById(eventId).orElseThrow(()
                -> new ObjectNotFoundException("Событие не найдено"));
        Comment comment = new Comment(event, user, commentDto.getText());
        comment.setCreatedTime(LocalDateTime.now());
        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Transactional
    public CommentDto updateComment(CommentDto commentDto, Long userId, Long commentId) {
        log.info("Редактируется комментарий");
        Comment comment = commentRepository.findById(commentId).orElseThrow(()
                -> new ObjectNotFoundException("Такого комментария не существует"));
        if (!comment.getUser().getId().equals(userId)) {
            log.info("Пользователь редактирует чужой комментарий");
            throw new UserWrongPropertiesException("Нельзя редактировать чужой комментарий");
        }
        comment.setText(commentDto.getText());
        comment.setUpdatedTime(LocalDateTime.now());
        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    public CommentDto getComment(Long commentId) {
        log.info("Получение комментария");
        log.info("Комментарий получен");
        return CommentMapper.toCommentDto(commentRepository.findById(commentId).orElseThrow(()
                -> new ObjectNotFoundException("Такого комментария нет")));
    }

    @Override
    public List<CommentDto> getComments(Long eventId, String byTime, Integer from, Integer size) {
        log.info("Получение списка комментариев события id: {}", eventId);
        if (!eventRepository.existsById(eventId)) {
            log.info("Такого события нет");
            throw new ObjectNotFoundException("Событие еще неопубликовано");
        }
        Pageable pageable = PageRequest.of(from, size);
        List<Comment> comments = commentRepository.findAllByEvent_IdOrderById(eventId, pageable);
        switch (byTime) {
            case "asc":
                comments.sort(Comparator.comparing(Comment::getCreatedTime));
                break;
            case "desc":
                comments.sort(Comparator.comparing(Comment::getCreatedTime).reversed());
                break;
        }
        return comments.stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }
}
