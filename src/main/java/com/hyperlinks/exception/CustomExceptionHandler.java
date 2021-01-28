package com.hyperlinks.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class CustomExceptionHandler {

    @Value("${errors.exceptionClass.send}")
    private boolean sendExceptionClass;

    @ExceptionHandler(EntityNotFoundException.class)
    public final ResponseEntity<ErrorDetails> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date().getTime(), "The Required Entity Was Not Found", ex.getClass(), request.getDescription(false), sendExceptionClass);
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class) //TODO maybe delete?
    public final ResponseEntity<ErrorDetails> handleException(Exception ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date().getTime(), "Internal Server Error", ex.getClass(), request.getDescription(false), sendExceptionClass);
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    public final ResponseEntity<? extends ErrorDetails> handleRuntimeException(RuntimeException ex, WebRequest request){
        ErrorDetails errorDetails = new ErrorDetails(new Date().getTime(), "Internal Server Error", ex.getClass(), request.getDescription(false), sendExceptionClass);
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public final ResponseEntity<? extends ErrorDetails> handleAccessDeniedException(AccessDeniedException ex, WebRequest request){
        ErrorDetails errorDetails = new ErrorDetails(new Date().getTime(), "Access Denied", ex.getClass(), request.getDescription(false), sendExceptionClass);
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity<? extends ErrorDetails> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request){
        List<String> validationErrors = ex.getBindingResult().getAllErrors().stream()
                .map(ObjectError::getCode)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        ValidationErrorDetails errorDetails = new ValidationErrorDetails(new Date().getTime(), "Invalid Object Received", validationErrors, ex.getClass(), request.getDescription(false), sendExceptionClass);
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BindException.class)
    public final ResponseEntity<? extends ErrorDetails> handleBindException(BindException ex, WebRequest request){
        List<String> validationErrors = ex.getGlobalErrors().stream()
                .map(ObjectError::getCode)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        ValidationErrorDetails errorDetails = new ValidationErrorDetails(new Date().getTime(), "Invalid Object Received", validationErrors, ex.getClass(), request.getDescription(false), sendExceptionClass);
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }


}
