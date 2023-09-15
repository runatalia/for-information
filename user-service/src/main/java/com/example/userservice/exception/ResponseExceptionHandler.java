package com.example.userservice.exception;

import jakarta.persistence.EntityExistsException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class ResponseExceptionHandler {
    private static final String BAD_REQUEST_MES = "Invalid data.";

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(NotFoundException ex, WebRequest request) {
        return new ErrorResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                request.getDescription(false)
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {
        List<ErrorResponse.FieldError> fieldErrors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(fieldError ->
                        new ErrorResponse.FieldError(fieldError.getField(), fieldError.getDefaultMessage())
                )
                .toList();

        return new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                BAD_REQUEST_MES,
                request.getDescription(false),
                fieldErrors
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
        List<ErrorResponse.FieldError> fieldErrors = ex.getConstraintViolations()
                .stream()
                .map(fieldError ->
                        new ErrorResponse.FieldError(fieldError.getPropertyPath().toString(), fieldError.getMessage())
                )
                .toList();

        return new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                BAD_REQUEST_MES,
                request.getDescription(false),
                fieldErrors
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(HttpMessageNotReadableException ex, WebRequest request) {
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                request.getDescription(false)
        );
    }

    @ExceptionHandler(EntityExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleEntityExistsException(EntityExistsException ex, WebRequest request) {
        return new ErrorResponse(
                HttpStatus.CONFLICT,
                ex.getMessage(),
                request.getDescription(false)
        );
    }

    @ExceptionHandler({InternalServerErrorException.class, UnsupportedOperationException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleEntityExistsException(RuntimeException ex, WebRequest request) {
        return new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage(),
                request.getDescription(false)
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                request.getDescription(false)
        );
    }

    //TODO: Что-то с этим придумать
    @ExceptionHandler(ClientErrorException.class)
    public ResponseEntity<ErrorResponse> handleClientErrorException(ClientErrorException ex, WebRequest request) {
        var httpStatus = HttpStatus.valueOf(ex.getResponse().getStatus());
        var errorResponse = new ErrorResponse(
                httpStatus,
                null,
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorResponse, httpStatus);
    }
}
