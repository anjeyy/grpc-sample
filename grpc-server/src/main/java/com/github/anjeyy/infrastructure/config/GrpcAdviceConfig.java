package com.github.anjeyy.infrastructure.config;

import com.github.anjeyy.infrastructure.exception.handler.GrpcExceptionHandlerMethodResolver;
import com.github.anjeyy.infrastructure.exception.handler.GrpcServiceAdviceDiscoverer;
import com.github.anjeyy.infrastructure.exception.handler.GrpcServiceAdviceExceptionHandler;
import com.github.anjeyy.infrastructure.exception.handler.GrpcServiceAdviceIsPresent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
@Conditional(GrpcServiceAdviceIsPresent.class)
public class GrpcAdviceConfig {

    @Bean
    GrpcServiceAdviceDiscoverer grpcServiceAdviceDiscoverer() {
        return new GrpcServiceAdviceDiscoverer();
    }

    @Bean
    GrpcExceptionHandlerMethodResolver grpcExceptionHandlerMethodResolver(
        final GrpcServiceAdviceDiscoverer grpcServiceAdviceDiscoverer) {
        return new GrpcExceptionHandlerMethodResolver(grpcServiceAdviceDiscoverer);
    }

    @Bean
    GrpcServiceAdviceExceptionHandler grpcServiceAdviceExceptionHandler(
        GrpcExceptionHandlerMethodResolver grpcExceptionHandlerMethodResolver) {
        return new GrpcServiceAdviceExceptionHandler(grpcExceptionHandlerMethodResolver);
    }

}
