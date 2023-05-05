package ru.practicum.mapper.comment;

import ru.practicum.dto.comment.CommentDto;
import ru.practicum.mapper.user.UserMapper;
import ru.practicum.model.comment.Comment;

public class CommentMapper {

    public static CommentDto toCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto(comment.getId(), comment.getEvent().getId(),
                UserMapper.toUserShortDto(comment.getUser()), comment.getText(), comment.getCreatedTime());
        if (comment.getUpdatedTime() != null) {
            commentDto.setUpdateTime(comment.getUpdatedTime());
        }
        return commentDto;
    }
}
