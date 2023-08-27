package com.fitplanner.authentication.exception;

import com.fitplanner.authentication.exception.model.UserAlreadyExistException;
import com.fitplanner.authentication.exception.model.UserNotFoundException;
import com.fitplanner.authentication.model.api.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiError> handleMethodNotSupportedException(
        HttpRequestMethodNotSupportedException ex,
        HttpServletRequest request
    ) {
        ApiError apiError = new ApiError(
            request.getRequestURI(),
            ex.getMessage(),
            HttpStatus.METHOD_NOT_ALLOWED.value(),
            LocalDateTime.now().toString()
        );

        return new ResponseEntity<>(apiError, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiError> handleUserNotFoundException(
        UserNotFoundException ex,
        HttpServletRequest request
    ) {
        ApiError apiError = new ApiError(
            request.getRequestURI(),
            ex.getMessage(),
            HttpStatus.NOT_FOUND.value(),
            LocalDateTime.now().toString()
        );

        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<ApiError> handleUserAlreadyExistException(
        UserAlreadyExistException ex,
        HttpServletRequest request
    ) {
        ApiError apiError = new ApiError(
            request.getRequestURI(),
            ex.getMessage(),
            HttpStatus.CONFLICT.value(),
            LocalDateTime.now().toString()
        );

        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }
}
