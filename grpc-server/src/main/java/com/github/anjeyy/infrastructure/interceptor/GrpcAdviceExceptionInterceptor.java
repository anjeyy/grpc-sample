package com.github.anjeyy.infrastructure.interceptor;

import com.github.anjeyy.infrastructure.exception.handler.GrpcAdviceExceptionHandler;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCall.Listener;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import lombok.RequiredArgsConstructor;

/**
 * Interceptor to use for global exception handling. Every raised {@link Throwable} is caught and being processed.
 * Actual processing of exception is in {@link GrpcAdviceExceptionListener}.
 * <p>
 *
 * @author Andjelko Perisic (andjelko.perisic@gmail.com)
 * @see GrpcAdviceExceptionHandler
 * @see GrpcAdviceExceptionListener
 */
@RequiredArgsConstructor
public class GrpcAdviceExceptionInterceptor implements ServerInterceptor {

    private final GrpcAdviceExceptionHandler exceptionHandler;

    @Override
    public <ReqT, RespT> Listener<ReqT> interceptCall(
        ServerCall<ReqT, RespT> call,
        Metadata headers,
        ServerCallHandler<ReqT, RespT> next
    ) {
        ServerCall.Listener<ReqT> delegate = next.startCall(call, headers);
        return new GrpcAdviceExceptionListener<>(delegate, call, exceptionHandler);
    }

}
