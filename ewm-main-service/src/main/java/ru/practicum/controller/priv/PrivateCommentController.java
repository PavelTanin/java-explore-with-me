package ru.practicum.controller.priv;


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
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/comments")
@RequiredArgsConstructor
public class PrivateCommentController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final CommentService commentService;

    @PostMapping("/{eventId}")
    public ResponseEntity<CommentDto> addComment(@Valid @RequestBody CommentDto commentDto,
                                                 @Positive @PathVariable(name = "userId") Long userId,
                                                 @Positive @PathVariable(name = "eventId") Long eventId) {
        log.info("Получен POST-запрос от пользователя id: {} на добавление комментария '{}' к событию id: {}",
                userId, commentDto.getText(), eventId);
        return new ResponseEntity<>(commentService.addComment(commentDto, userId, eventId), HttpStatus.CREATED);
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@Valid @RequestBody CommentDto commentDto,
                                                    @Positive @PathVariable(name = "userId") Long userId,
                                                    @Positive @PathVariable(name = "commentId") Long commentId) {
        log.info("Получен PATCH-запрос от пользователя id: {} на изменение комментария id: {}", userId, commentId);
        return new ResponseEntity<>(commentService.updateCommentByUser(commentDto, userId, commentId), HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@Positive @PathVariable(name = "userId") Long userId,
                                                @Positive @PathVariable(name = "commentId") Long commentId) {
        log.info("Получен DELETE-запрос от пользователя id: {} на удаление комментария id: {}", userId, commentId);
        return new ResponseEntity<>(commentService.deleteCommentByAuthor(userId, commentId), HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentDto> getAuthorComment(@Positive @PathVariable(name = "userId") Long userId,
                                                @Positive @PathVariable(name = "commentId") Long commentId) {
        log.info("Получен GET-запрос на просмотр комментария id: {} от пользователя id: {}", commentId, userId);
        return new ResponseEntity<>(commentService.getUserComment(userId, commentId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<CommentDto>> getAllUserComments(@Positive @PathVariable(name = "userId") Long userId) {
        log.info("Получен GET-запрос от пользователя id: {} на получение списка своих комментариев");
        return new ResponseEntity<>(commentService.getAllUserComments(userId), HttpStatus.OK);
    }
}
