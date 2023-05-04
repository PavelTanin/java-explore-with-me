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
        return new ResponseEntity<>(commentService.updateComment(commentDto, userId, commentId), HttpStatus.OK);
    }
}
