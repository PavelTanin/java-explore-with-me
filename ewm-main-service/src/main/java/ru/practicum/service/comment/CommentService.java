package ru.practicum.service.comment;

import ru.practicum.dto.comment.CommentDto;

import java.util.List;

public interface CommentService {
    CommentDto addComment(CommentDto commentDto, Long userId, Long eventId);

    CommentDto updateComment(CommentDto commentDto, Long userId, Long commentId);

    CommentDto getComment(Long commentId);

    List<CommentDto> getComments(Long eventId, String byTime, Integer from, Integer size);
}
