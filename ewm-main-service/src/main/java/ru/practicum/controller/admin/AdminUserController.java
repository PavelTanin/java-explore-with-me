package ru.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.user.NewUserDto;
import ru.practicum.service.user.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final UserService userService;

    @PostMapping
    public ResponseEntity addUser(@RequestBody @Valid NewUserDto newUserDto) {
        log.info("От администратора получен POST-запрос на добавление нового пользователя: {}", newUserDto.toString());
        return new ResponseEntity<>(userService.addUser(newUserDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity deleteUser(@PathVariable Long userId) {
        log.info("От администратора получен DELETE-запрос на удаление пользователя: {}", userId);
        return new ResponseEntity<>(new String[]{userService.deleteUser(userId)}, HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity getUser(@RequestParam(required = false, name = "ids") List<Long> ids,
                                  @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                  @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("От администратора получен GET-запрос на получение информации о пользователях: {}", ids.toString());
        return new ResponseEntity<>(userService.getUsers(ids, from, size), HttpStatus.OK);
    }
}
