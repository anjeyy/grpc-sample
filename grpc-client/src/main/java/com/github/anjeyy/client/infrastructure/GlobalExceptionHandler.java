package com.github.anjeyy.client.infrastructure;

import io.grpc.StatusRuntimeException;
import java.util.Optional;
import org.springframework.core.NestedRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        String errMsg = unwrapNestedException(e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errMsg);
    }

    @ExceptionHandler(StatusRuntimeException.class)
    public ResponseEntity<String> handleConstraintViolationException(StatusRuntimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    private <E extends NestedRuntimeException> String unwrapNestedException(E exception) {
        return Optional
            .of(exception)
            .map(NestedRuntimeException::getRootCause)
            .map(Throwable::getMessage)
            .orElseGet(exception::getMessage);
    }
}
