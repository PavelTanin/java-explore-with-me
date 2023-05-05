package ru.practicum.service.comment;

import ru.practicum.dto.comment.CommentDto;

import java.util.List;

public interface CommentService {
    CommentDto addComment(CommentDto commentDto, Long userId, Long eventId);

    CommentDto updateCommentByUser(CommentDto commentDto, Long userId, Long commentId);

    CommentDto getComment(Long commentId);

    List<CommentDto> getComments(Long eventId, String byTime, Integer from, Integer size);

    String deleteCommentByAuthor(Long userId, Long commentId);

    CommentDto getUserComment(Long userId, Long commentId);

    List<CommentDto> getAllUserComments(Long userId);

    CommentDto updateCommentByAdmin(CommentDto commentDto, Long commentId);

    String deleteCommentByAdmin(Long commentId);
}
