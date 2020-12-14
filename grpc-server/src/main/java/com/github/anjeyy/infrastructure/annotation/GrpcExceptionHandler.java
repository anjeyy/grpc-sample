package com.github.anjeyy.infrastructure.annotation;

import com.github.anjeyy.infrastructure.exception.handler.GrpcExceptionHandlerMethodResolver;
import com.github.anjeyy.infrastructure.exception.handler.GrpcServiceAdviceExceptionHandler;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Methods annotated with {@link GrpcExceptionHandler @GrpcExceptionHandler} are being mapped to a corresponding
 * Exception, by declaring either in {@link GrpcExceptionHandler#value() @GrpcExceptionHandler(value = ...)} as value or
 * as annotated methods parameter (both is working too).
 * <p>
 * Return type of annotated methods has to be of type {@link Throwable} or {@link io.grpc.Status}, the latter is wrapped
 * up later as {@link io.grpc.StatusRuntimeException}. For more detailed information
 * {@link GrpcExceptionHandlerMethodResolver}. <br>
 * <p>
 * <p>
 * As an example, this is the preferred way of handling exception,
 *
 * <pre>
 * {@code @GrpcExceptionHandler
 *    public Status handleIllegalArgumentException(IllegalArgumentException e){
 *      return Status.INVALID_ARGUMENT
 *                   .withDescription(e.getMessage())
 *                   .withCause(e);
 *    }
 *  }
 * </pre>
 * <p>
 * but the following is also possible, especially if {@link io.grpc.Metadata} has to be returned.
 *
 * <pre>
 * {@code @GrpcExceptionHandler
 *    public StatusRuntimeException handleIllegalArgumentException(IllegalArgumentException e){
 *      Status status = Status.INVALID_ARGUMENT
 *                            .withDescription(e.getMessage())
 *                            .withCause(e);
 *      return status.asRuntimeException();
 *    }
 *  }
 * </pre>
 * <p>
 * Further when an {@link Exception} is raised by the application during runtime,
 * {@link GrpcServiceAdviceExceptionHandler} interrupts after thrown exception and executes above mentioned annotated
 * method which was mapped by {@link GrpcExceptionHandler @GrpcExceptionHandler} inside a class annotated with
 * {@link GrpcServiceAdvice @GrpcServiceAdvice}.<br>
 * <p>
 *
 * @author Andjelko Perisic (andjelko.perisic@gmail.com)
 * @see GrpcServiceAdvice
 * @see GrpcExceptionHandlerMethodResolver
 * @see GrpcServiceAdviceExceptionHandler
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GrpcExceptionHandler {

    /**
     * Exceptions handled by the annotated method.
     * <p>
     * If empty, will default to any exceptions listed in the method argument list.
     */
    Class<? extends Throwable>[] value() default {};
}
