package com.github.anjeyy.api.annotation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Exception handling for thrown {@link RuntimeException}
 * inside gRPC service classes, which implements {@link io.grpc.BindableService}.
 */
@Slf4j
@Configuration
public class GrpcExceptionAdvice {

    //TODO example class

    /*
     * 1. exception handling annotation
     * 2. exception thrown inside implemented BindableService class
     * 3. process mapped exception Handling
     */


    @ExceptionHandler
    public void doSome() {

    }


    @GrpcExceptionHandler(IllegalArgumentException.class)
    public void doSomeGrpc(IllegalArgumentException e) {
        
    }

}
