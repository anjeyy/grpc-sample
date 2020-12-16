package com.github.anjeyy.infrastructure.annotation;

import com.github.anjeyy.infrastructure.exception.handler.GrpcAdviceExceptionHandler;
import com.github.anjeyy.infrastructure.exception.handler.GrpcExceptionHandlerMethodResolver;
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
 * Return type of annotated methods has to be of type {@link io.grpc.Status}, {@link io.grpc.StatusException},
 * {@link io.grpc.StatusRuntimeException} or {@link Throwable}.
 * <p>
 * <p>
 * An example without {@link io.grpc.Metadata}:
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
 *
 * <b>With</b> {@link io.grpc.Metadata}:
 *
 * <pre>
 * {@code @GrpcExceptionHandler
 *    public StatusRuntimeException handleIllegalArgumentException(IllegalArgumentException e){
 *      Status status = Status.INVALID_ARGUMENT
 *                            .withDescription(e.getMessage())
 *                            .withCause(e);
 *      Metadata myMetadata = new Metadata();
 *      myMetadata = ...
 *      return status.asRuntimeException(myMetadata);
 *    }
 *  }
 * </pre>
 *
 * @author Andjelko Perisic (andjelko.perisic@gmail.com)
 * @see GrpcAdvice
 * @see GrpcExceptionHandlerMethodResolver
 * @see GrpcAdviceExceptionHandler
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GrpcExceptionHandler {

    /**
     * Exceptions handled by the annotated method.
     * <p>
     * If empty, will default to any exceptions listed in the method argument list.
     * <p>
     * <b>Note:</b> When exception types are set within value, they are prioritized in mapping the exceptions over
     * listed method arugments. And in case method arguments are provided, they <b>must</b> match the types declared
     * with this value.
     */
    Class<? extends Throwable>[] value() default {};
}
