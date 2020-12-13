package com.github.anjeyy.infrastructure.exception;

import com.github.anjeyy.infrastructure.annotation.GrpcExceptionHandler;
import com.github.anjeyy.infrastructure.annotation.GrpcServiceAdvice;
import io.grpc.Status;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@GrpcServiceAdvice
public class GrpcExceptionAdvice {


    @GrpcExceptionHandler
    public Throwable handleIllegalArgumentException(IllegalArgumentException e) {
        Status status = Status.INVALID_ARGUMENT.withDescription(e.getMessage()).withCause(e);

        return status.asRuntimeException();
    }

    @GrpcExceptionHandler(ResourceNotFoundException.class)
    public Throwable handleResourceNotFoundException(ResourceNotFoundException e) {
        Status status = Status.NOT_FOUND.withDescription(e.getMessage()).withCause(e);

        return status.asException();
    }
}
