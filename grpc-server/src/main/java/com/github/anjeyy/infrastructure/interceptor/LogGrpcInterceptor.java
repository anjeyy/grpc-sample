package com.github.anjeyy.infrastructure.interceptor;

import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCall.Listener;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;

@Slf4j
@GrpcGlobalServerInterceptor
public class LogGrpcInterceptor implements ServerInterceptor {

    @Override
    public <ReqT, RespT> Listener<ReqT> interceptCall(
        ServerCall<ReqT, RespT> serverCall,
        Metadata headers,
        ServerCallHandler<ReqT, RespT> next) {

        log.info("Methoddescription: {}", serverCall.getMethodDescriptor().getFullMethodName());
        return next.startCall(serverCall, headers);
    }
}
