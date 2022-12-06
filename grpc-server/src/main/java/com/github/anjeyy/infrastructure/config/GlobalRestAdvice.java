package com.github.anjeyy.infrastructure.config;

import com.github.anjeyy.infrastructure.exception.ResourceNotFoundException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.core.NestedRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Optional;

@ControllerAdvice
public class GlobalRestAdvice {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        String errMsg = unwrapNestedException(e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(errMsg);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(e.getMessage());
    }

    private <E extends NestedRuntimeException> String unwrapNestedException(E exception) {
        return Optional.of(exception)
                       .map(NestedRuntimeException::getRootCause)
                       .map(Throwable::getMessage)
                       .orElseGet(exception::getMessage);
    }
}
