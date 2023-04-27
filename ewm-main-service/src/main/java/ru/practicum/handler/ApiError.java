package ru.practicum.handler;


import lombok.Data;

import java.time.LocalDateTime;


@Data
public class ApiError {

    private String status;

    private String reason;
    private String message;
    private String timestamp;

    public ApiError(String status, String reason, String message, String timestamp) {
        this.status = status;
        this.reason = reason;
        this.message = message;
        this.timestamp = timestamp;
    }
}
