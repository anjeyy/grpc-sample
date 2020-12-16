package com.github.anjeyy.infrastructure.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.stereotype.Component;

/**
 * Special {@link Component @Component} to declare global gRPC exception handling.
 * <p>
 * Every class annotated with {@link GrpcAdvice @GrpcAdvice} is marked to be scanned for
 * {@link GrpcExceptionHandler @GrpcExceptionHandler} annotations.
 * <p>
 *
 * @author Andjelko Perisic (andjelko.perisic@gmail.com)
 * @see GrpcExceptionHandler
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface GrpcAdvice {

}
