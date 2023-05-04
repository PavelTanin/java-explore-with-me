package ru.practicum.mapper.comment;

import ru.practicum.dto.comment.CommentDto;
import ru.practicum.model.comment.Comment;

public class CommentMapper {

    public static CommentDto toCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto(comment.getId(), comment.getEvent().getId(),
                comment.getUser().getId(), comment.getText(), comment.getCreatedTime());
        if (comment.getUpdatedTime() != null) {
            commentDto.setUpdateTime(comment.getUpdatedTime());
        }
        return commentDto;
    }
}
