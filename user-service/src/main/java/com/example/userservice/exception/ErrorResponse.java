package com.example.userservice.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private List<FieldError> details;
    private String uri;

    public ErrorResponse(HttpStatus httpStatus, String message, String uri) {
        this.timestamp = LocalDateTime.now();
        this.status = httpStatus.value();
        this.error = httpStatus.getReasonPhrase();
        this.message = message;
        this.uri = uri.substring(uri.indexOf("/"));
    }

    public ErrorResponse(HttpStatus httpStatus, String message, String uri, List<FieldError> fieldErrors) {
        this(httpStatus, message, uri);
        this.details = fieldErrors;
    }

    record FieldError(String field, String message) {
    }
}