package ru.practicum.controller.pub;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.service.comment.CommentService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class PublicCommentController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final CommentService commentService;

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentDto> getComments(@Positive @PathVariable(name = "commentId") Long commentId) {
        log.info("Получен GET-запрос на получение информации о комментарие id: {}", commentId);
        return new ResponseEntity<>(commentService.getComment(commentId), HttpStatus.OK);
    }

    @GetMapping("/events/{eventId}")
    public ResponseEntity<List<CommentDto>> getComments(@Positive @PathVariable(name = "eventId") Long eventId,
                                                        @RequestParam(name = "byTime", defaultValue = "asc") String byTime,
                                                        @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                        @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Получен GET-запрос на получение всех комментариев для события id: {}", eventId);
        return new ResponseEntity<>(commentService.getComments(eventId, byTime, from, size), HttpStatus.OK);
    }
}
