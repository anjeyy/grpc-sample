package com.github.anjeyy.api.annotation;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Exception handling for thrown {@link RuntimeException}
 * inside gRPC service classes, which implements {@link io.grpc.BindableService}.
 */
@Slf4j
@Aspect
@Component
public class GrpcExceptionAspect {

    @Pointcut("within(@net.devh.boot.grpc.server.service.GrpcService *)")
    void grpcServiceAnnotatedPointcut() {
    }

    @Pointcut("within(io.grpc.BindableService+)")
    void implementedBindableServicePointcut() {
    }

    @AfterThrowing(
        pointcut = "grpcServiceAnnotatedPointcut() && implementedBindableServicePointcut()",
        throwing = "runtimeException"
    )

    public <E extends RuntimeException> void exitRunningApplication(JoinPoint joinPoint, E runtimeException) {
        log.error("Runtimeexception caught during gRPC service execution: ", runtimeException);

        //TODO caught exception and map with other annotated method to execute

        System.out.println("############");

    }

}
