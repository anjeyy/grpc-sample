package com.github.anjeyy.infrastructure.config;

import com.github.anjeyy.infrastructure.exception.ResourceNotFoundException;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;

@GrpcAdvice
public class GlobalGrpcAdvice {

    @GrpcExceptionHandler(IllegalArgumentException.class)
    public Status handleIllegalArgumentException(IllegalArgumentException e) {
        Status status = Status.INVALID_ARGUMENT.withDescription(e.getMessage()).withCause(e);

        return status;
    }

    @GrpcExceptionHandler(ResourceNotFoundException.class)
    public StatusRuntimeException handleResourceNotFoundException(ResourceNotFoundException e) {
        Status status = Status.NOT_FOUND.withDescription(e.getMessage()).withCause(e);

        return status.asRuntimeException();
    }

    @GrpcExceptionHandler(RuntimeException.class)
    public StatusRuntimeException handleRuntimeException(RuntimeException e) {
        Status status = Status.NOT_FOUND.withDescription(e.getMessage()).withCause(e);

        return status.asRuntimeException();
    }
}
