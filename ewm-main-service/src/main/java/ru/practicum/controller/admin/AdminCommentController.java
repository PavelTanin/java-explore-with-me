package ru.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.service.comment.CommentService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/admin/comments/{commentId}")
@RequiredArgsConstructor
public class AdminCommentController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final CommentService commentService;

    @PatchMapping
    public ResponseEntity<CommentDto> updateComment(@Valid @RequestBody CommentDto commentDto,
                                                    @Positive @PathVariable(name = "commentId") Long commentId) {
        log.info("Получен PATCH-запрос от администратора на изменение комментария id: {}", commentId);
        return new ResponseEntity<>(commentService.updateCommentByAdmin(commentDto, commentId), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteCommentByAdmin(@Positive @PathVariable(name = "commentId") Long commentId) {
        log.info("Получен DELETE-запрос от администратора на удаление комментария id: {}", commentId);
        return new ResponseEntity<>(commentService.deleteCommentByAdmin(commentId), HttpStatus.NO_CONTENT);
    }
}
