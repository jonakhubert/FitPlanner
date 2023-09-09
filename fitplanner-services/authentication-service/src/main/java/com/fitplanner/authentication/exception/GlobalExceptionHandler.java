package com.fitplanner.authentication.exception;

import com.fitplanner.authentication.exception.model.*;
import com.fitplanner.authentication.model.api.ApiError;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mail.MailSendException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.ConnectException;
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

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleHttpMessageNotReadableException(
        HttpMessageNotReadableException ex,
        HttpServletRequest request
    ) {
        ApiError apiError = new ApiError(
            request.getRequestURI(),
            ex.getMessage(),
            HttpStatus.BAD_REQUEST.value(),
            LocalDateTime.now().toString()
        );

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiError> handleNotSupportedMediaTypeException(
        HttpMediaTypeNotSupportedException ex,
        HttpServletRequest request
    ) {
        ApiError apiError = new ApiError(
            request.getRequestURI(),
            ex.getMessage(),
            HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(),
            LocalDateTime.now().toString()
        );

        return new ResponseEntity<>(apiError, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException ex,
        HttpServletRequest request
    ) {
        ApiError apiError = new ApiError(
            request.getRequestURI(),
            ex.getMessage(),
            HttpStatus.BAD_REQUEST.value(),
            LocalDateTime.now().toString()
        );

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidEmailFormatException.class)
    public ResponseEntity<ApiError> handleInvalidEmailFormatException(
        InvalidEmailFormatException ex,
        HttpServletRequest request
    ) {
        ApiError apiError = new ApiError(
            request.getRequestURI(),
            ex.getMessage(),
            HttpStatus.BAD_REQUEST.value(),
            LocalDateTime.now().toString()
        );

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleBadCredentialsException(
        BadCredentialsException ex,
        HttpServletRequest request
    ) {
        ApiError apiError = new ApiError(
            request.getRequestURI(),
            ex.getMessage(),
            HttpStatus.BAD_REQUEST.value(),
            LocalDateTime.now().toString()
        );

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConnectException.class)
    public ResponseEntity<ApiError> handleConnectException(
        ConnectException ex,
        HttpServletRequest request
    ) {
        ApiError apiError = new ApiError(
            request.getRequestURI(),
            ex.getMessage(),
            HttpStatus.SERVICE_UNAVAILABLE.value(),
            LocalDateTime.now().toString()
        );

        return new ResponseEntity<>(apiError, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ApiError> handleTokenExpiredException(
        TokenExpiredException ex,
        HttpServletRequest request
    ) {
        ApiError apiError = new ApiError(
            request.getRequestURI(),
            ex.getMessage(),
            HttpStatus.UNAUTHORIZED.value(),
            LocalDateTime.now().toString()
        );

        return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserAlreadyVerifiedException.class)
    public ResponseEntity<ApiError> handleUserAlreadyVerifiedException(
        UserAlreadyVerifiedException ex,
        HttpServletRequest request
    ) {
        ApiError apiError = new ApiError(
            request.getRequestURI(),
            ex.getMessage(),
            HttpStatus.OK.value(),
            LocalDateTime.now().toString()
        );

        return new ResponseEntity<>(apiError, HttpStatus.OK);
    }

    @ExceptionHandler(UserNotVerifiedException.class)
    public ResponseEntity<ApiError> handleUserNotVerifiedException(
        UserNotVerifiedException ex,
        HttpServletRequest request
    ) {
        ApiError apiError = new ApiError(
            request.getRequestURI(),
            ex.getMessage(),
            HttpStatus.FORBIDDEN.value(),
            LocalDateTime.now().toString()
        );

        return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MailSendException.class)
    public ResponseEntity<ApiError> handleMessagingException(
        MessagingException ex,
        HttpServletRequest request
    ) {
        ApiError apiError = new ApiError(
            request.getRequestURI(),
            ex.getMessage(),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            LocalDateTime.now().toString()
        );

        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(TokenNotFoundException.class)
    public ResponseEntity<ApiError> handleTokenNotFoundException(
        TokenNotFoundException ex,
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
}
