package com.github.anjeyy.infrastructure.config;

import com.github.anjeyy.infrastructure.annotation.GrpcServiceAdvice;
import com.github.anjeyy.infrastructure.exception.handler.GrpcExceptionAspect;
import com.github.anjeyy.infrastructure.exception.handler.GrpcExceptionHandlerMethodResolver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

//@Configuration
public class AbcConfig {

    @ConditionalOnBean(annotation = GrpcServiceAdvice.class)
    @Bean
    public GrpcExceptionAspect grpcExceptionHandling(
        GrpcExceptionHandlerMethodResolver grpcExceptionHandlerMethodResolver) {
        return new GrpcExceptionAspect(grpcExceptionHandlerMethodResolver);
    }

    @ConditionalOnBean(annotation = GrpcServiceAdvice.class)
    @Bean
    public GrpcExceptionHandlerMethodResolver grpcExceptionHandling2(final ApplicationContext applicationContext) {
        return new GrpcExceptionHandlerMethodResolver(applicationContext);
    }

}
