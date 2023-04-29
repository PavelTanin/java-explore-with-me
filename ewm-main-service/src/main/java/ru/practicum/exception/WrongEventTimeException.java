package ru.practicum.exception;

public class WrongEventTimeException extends RuntimeException {

    public WrongEventTimeException(String message) {
        super(message);
    }
}
