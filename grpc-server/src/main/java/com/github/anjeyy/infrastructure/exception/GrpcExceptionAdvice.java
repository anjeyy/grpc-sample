package com.github.anjeyy.infrastructure.exception;

import com.github.anjeyy.infrastructure.annotation.GrpcAdvice;
import com.github.anjeyy.infrastructure.annotation.GrpcExceptionHandler;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@GrpcAdvice
public class GrpcExceptionAdvice {


    @GrpcExceptionHandler
    public Status handleIllegalArgumentException(IllegalArgumentException e) {
        Status status = Status.INVALID_ARGUMENT.withDescription(e.getMessage()).withCause(e);

        return status;
    }

    @GrpcExceptionHandler(ResourceNotFoundException.class)
    public StatusRuntimeException handleResourceNotFoundException(ResourceNotFoundException e) {
        Status status = Status.NOT_FOUND.withDescription(e.getMessage()).withCause(e);

        return status.asRuntimeException();
    }

}
