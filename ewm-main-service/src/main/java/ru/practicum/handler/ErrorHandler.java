package ru.practicum.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.exception.*;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity validationException(MethodArgumentNotValidException ex) {
        log.info("Некорректно заполненно поле {}", ex.getBindingResult().getFieldError().getField());
        ApiError ae = new ApiError(HttpStatus.BAD_REQUEST.name(), "Получен некорректный запрос",
                ex.getBindingResult().getFieldError().getDefaultMessage(), LocalDateTime.now().format(formatter));
        return new ResponseEntity(ae, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotInitiatorException.class)
    public ResponseEntity validationException(UserNotInitiatorException ex) {
        ApiError ae = new ApiError(HttpStatus.BAD_REQUEST.name(), "Пользователь не является инициатором", ex.getMessage(),
                LocalDateTime.now().format(formatter));
        return new ResponseEntity(ae, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity validationException(SQLException ex) {
        log.info("Некорректно заполненно поле");
        ApiError ae = new ApiError(HttpStatus.CONFLICT.name(), "Нарушена уникальность данных",
                ex.getMessage(), LocalDateTime.now().format(formatter));
        return new ResponseEntity(ae, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity validationException(ObjectNotFoundException ex) {
        log.info("Объект не найден");
        ApiError ae = new ApiError(HttpStatus.NOT_FOUND.name(), "Объект не найден",
                ex.getMessage(), LocalDateTime.now().format(formatter));
        return new ResponseEntity(ae, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({WrongEventTimeException.class, EventPubProblemException.class,
            UserWrongPropertiesException.class, RequestLimitException.class, CancelingRequestException.class})
    public ResponseEntity validationException(Exception ex) {
        ApiError ae = new ApiError(HttpStatus.CONFLICT.name(), "Конфликт данных", ex.getMessage(),
                LocalDateTime.now().format(formatter));
        return new ResponseEntity(ae, HttpStatus.CONFLICT);
    }

}
