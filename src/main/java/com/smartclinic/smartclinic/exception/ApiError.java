package com.smartclinic.smartclinic.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ApiError {

    private LocalDateTime timestamp;
    private int status;
    private String message;

    public static ApiError of(int status, String message) {
        return new ApiError(
                LocalDateTime.now(),
                status,
                message
        );
    }
}
