package com.github.anjeyy.infrastructure.validation;

import net.devh.boot.grpc.common.util.InterceptorOrder;
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * In Order to have valid requests this autoconfiguration is looking for marker annotation
 * {@link GrpcConstraint @GrpcConstraint}. In case of success, all necessary beans are being instantiated.
 *
 * @author Andjelko Perisic (andjelko.perisic@gmail.com)
 * @see GrpcConstraint
 * @see GrpcValidationResolver
 * @see RequestValidationInterceptor
 */
@Configuration
@Conditional(GrpcConstraintIsPresent.class)
class GrpcValidationConfig {

    @Bean
    GrpcValidationResolver grpcValidationResolver() {
        return new GrpcValidationResolver();
    }


    @GrpcGlobalServerInterceptor
    @Order(InterceptorOrder.ORDER_GLOBAL_EXCEPTION_HANDLING - 15000)
    RequestValidationInterceptor requestValidationInterceptor(final GrpcValidationResolver grpcValidationResolver) {
        return new RequestValidationInterceptor(grpcValidationResolver);
    }

}
