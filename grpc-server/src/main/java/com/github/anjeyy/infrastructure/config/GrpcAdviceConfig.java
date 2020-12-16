package com.github.anjeyy.infrastructure.config;

import com.github.anjeyy.infrastructure.annotation.GrpcAdvice;
import com.github.anjeyy.infrastructure.annotation.GrpcExceptionHandler;
import com.github.anjeyy.infrastructure.exception.handler.GrpcAdviceDiscoverer;
import com.github.anjeyy.infrastructure.exception.handler.GrpcAdviceExceptionHandler;
import com.github.anjeyy.infrastructure.exception.handler.GrpcExceptionHandlerMethodResolver;
import com.github.anjeyy.infrastructure.exception.handler.GrpcServiceAdviceIsPresent;
import com.github.anjeyy.infrastructure.interceptor.GrpcAdviceExceptionInterceptor;
import net.devh.boot.grpc.common.util.InterceptorOrder;
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * The auto configuration that will create necessary beans to provide a proper exception handling via annotations
 * {@link GrpcAdvice @GrpcServiceAdvice} and {@link GrpcExceptionHandler @GrpcExceptionHandler}
 * <p>
 *
 * @author Andjelko Perisic (andjelko.perisic@gmail.com)
 * @see GrpcAdvice
 * @see GrpcExceptionHandler
 */
@Configuration
@Conditional(GrpcServiceAdviceIsPresent.class)
public class GrpcAdviceConfig {

    @Bean
    GrpcAdviceDiscoverer grpcServiceAdviceDiscoverer() {
        return new GrpcAdviceDiscoverer();
    }

    @Bean
    GrpcExceptionHandlerMethodResolver grpcExceptionHandlerMethodResolver(
        final GrpcAdviceDiscoverer grpcAdviceDiscoverer) {
        return new GrpcExceptionHandlerMethodResolver(grpcAdviceDiscoverer);
    }

    //TODO - here
    @Bean
    GrpcAdviceExceptionHandler grpcServiceAdviceExceptionHandler(
        GrpcExceptionHandlerMethodResolver grpcExceptionHandlerMethodResolver) {
        return new GrpcAdviceExceptionHandler(grpcExceptionHandlerMethodResolver);
    }

    @GrpcGlobalServerInterceptor
    @Order(InterceptorOrder.ORDER_GLOBAL_EXCEPTION_HANDLING)
    GrpcAdviceExceptionInterceptor grpcServiceAdviceExceptionInterceptor(
        GrpcAdviceExceptionHandler grpcAdviceExceptionHandler) {
        return new GrpcAdviceExceptionInterceptor(grpcAdviceExceptionHandler);
    }

    //TODO add interceptors

    //TODO run apply checks

}
