package ru.practicum.exception;

public class CancelingRequestException extends RuntimeException {

    public CancelingRequestException(String message) {
        super(message);
    }
}
