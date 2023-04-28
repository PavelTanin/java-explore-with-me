package ru.practicum.controller.priv;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.service.request.RequestService;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
public class PrivateRequestController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final RequestService requestService;

    @PostMapping
    public ResponseEntity<ParticipationRequestDto> addRequest(@Positive @PathVariable(name = "userId") Long userId,
                                                              @Positive @RequestParam(name = "eventId") Long eventId) {
        log.info("Получен POST-запрос от пользователя id: {} на участие в событии id: {}", userId, eventId);
        return new ResponseEntity<>(requestService.addRequest(userId, eventId), HttpStatus.CREATED);
    }

    @PatchMapping("/{requestId}/cancel")
    public ResponseEntity<ParticipationRequestDto> deleteRequest(@Positive @PathVariable(name = "userId") Long userId,
                                         @Positive @PathVariable(name = "requestId") Long requestId) {
        log.info("Получен DELETE-запрос от пользователя id: {} на удаление заявки id: {}", userId, requestId);
        return new ResponseEntity<>(requestService.deleteRequest(userId, requestId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ParticipationRequestDto>> getAllUserRequest(@Positive @PathVariable(name = "userId") Long userId) {
        log.info("Получен GET-запрос от пользователя id: {} на получение своих заявок", userId);
        return new ResponseEntity<>(requestService.getAllUserRequests(userId), HttpStatus.OK);
    }
}
