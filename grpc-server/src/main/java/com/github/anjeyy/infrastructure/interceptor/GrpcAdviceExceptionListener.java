package com.github.anjeyy.infrastructure.interceptor;

import com.github.anjeyy.infrastructure.annotation.GrpcExceptionHandler;
import com.github.anjeyy.infrastructure.exception.handler.GrpcAdviceExceptionHandler;
import io.grpc.ForwardingServerCallListener.SimpleForwardingServerCallListener;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCall.Listener;
import io.grpc.Status;
import io.grpc.StatusException;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;

/**
 * In case an exception is thrown inside {@link #onHalfClose()}, it is being handled by invoking annotated methods with
 * {@link GrpcExceptionHandler @GrpcExceptionHandler}. On successful invocation proper exception handling is done.
 * <p>
 * <b>Note:</b> In case of raised exceptions by implementation a {@link Status#INTERNAL} is returned in
 * {@link #handleThrownExceptionByImplementation(Throwable)}.
 *
 * @param <ReqT>  gRPC request type
 * @param <RespT> gRPC response type
 * @author Andjelko Perisic (andjelko.perisic@gmail.com)
 * @see GrpcAdviceExceptionHandler
 */
@Slf4j
public class GrpcAdviceExceptionListener<ReqT, RespT> extends SimpleForwardingServerCallListener<ReqT> {

    private final GrpcAdviceExceptionHandler exceptionHandler;
    private final ServerCall<ReqT, RespT> serverCall;

    protected GrpcAdviceExceptionListener(
        Listener<ReqT> delegate,
        ServerCall<ReqT, RespT> serverCall,
        GrpcAdviceExceptionHandler exceptionHandler
    ) {
        super(delegate);
        this.serverCall = serverCall;
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public void onHalfClose() {
        try {
            super.onHalfClose();
        } catch (Exception exception) {
            log.error("Exception caught during gRPC execution: ", exception);
            handleCaughtException(exception);
        }
    }

    private void handleCaughtException(Exception exception) {
        try {
            Object mappedReturnType = exceptionHandler.handleThrownException(exception);
            Status status = resolveStatus(mappedReturnType);
            Metadata metadata = resolveMetadata(mappedReturnType);

            serverCall.close(status, metadata);
        } catch (Throwable throwable) {
            handleThrownExceptionByImplementation(throwable);
        }
    }

    private Status resolveStatus(Object mappedReturnType) {
        if (mappedReturnType instanceof Status) {
            return (Status) mappedReturnType;
        } else if (mappedReturnType instanceof Throwable) {
            return Status.fromThrowable((Throwable) mappedReturnType);
        }
        throw new IllegalStateException(String.format(
            "Error for mapped return type [%s] inside @GrpcServiceAdvice, it has to be of type: "
                + "[Status, StatusException, StatusRuntimeException, Throwable] ", mappedReturnType));
    }

    private Metadata resolveMetadata(Object mappedReturnType) {
        Metadata result = null;
        if (mappedReturnType instanceof StatusException) {
            StatusException statusException = (StatusException) mappedReturnType;
            result = statusException.getTrailers();
        } else if (mappedReturnType instanceof StatusRuntimeException) {
            StatusRuntimeException statusException = (StatusRuntimeException) mappedReturnType;
            result = statusException.getTrailers();
        }
        return (result == null) ? new Metadata() : result;
    }

    private void handleThrownExceptionByImplementation(Throwable throwable) {
        serverCall.close(Status.INTERNAL.withCause(throwable)
                                        .withDescription(throwable.getMessage()), new Metadata());
    }

}
