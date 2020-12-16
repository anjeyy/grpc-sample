package com.github.anjeyy.infrastructure.exception.handler;

import com.github.anjeyy.infrastructure.annotation.GrpcAdvice;
import com.github.anjeyy.infrastructure.annotation.GrpcExceptionHandler;
import com.github.anjeyy.infrastructure.interceptor.GrpcAdviceExceptionInterceptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.Map.Entry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;

/**
 * As part of {@link GrpcAdvice @GrpcAdvice}, when a thrown exception is caught during gRPC calls (via global
 * interceptor {@link GrpcAdviceExceptionInterceptor}, then this thrown exception is being handled. By
 * {@link GrpcExceptionHandlerMethodResolver} is a mapping between exception and the in case to be executed method
 * provided. <br>
 * Returned object is declared in {@link GrpcAdvice @GrpcAdvice} classes with annotated methods
 * {@link GrpcExceptionHandler @GrpcExceptionHandler}.
 * <p>
 *
 * @author Andjelko Perisic (andjelko.perisic@gmail.com)
 * @see GrpcExceptionHandlerMethodResolver
 * @see GrpcAdviceExceptionInterceptor
 */
@Slf4j
public class GrpcAdviceExceptionHandler {

    private final GrpcExceptionHandlerMethodResolver grpcExceptionHandlerMethodResolver;

    public GrpcAdviceExceptionHandler(
        final GrpcExceptionHandlerMethodResolver grpcExceptionHandlerMethodResolver) {
        this.grpcExceptionHandlerMethodResolver = grpcExceptionHandlerMethodResolver;
    }

    /**
     * Given an exception, a lookup is performed to retrieve mapped method. <br>
     * In case of successful returned method, and matching exception parameter type for given exception, the exception
     * is handed over to retrieved method. Retrieved method is then being invoked.
     *
     * @param exception exception to search for
     * @param <E>       type of exception
     * @return result of invoked mapped method to given exception
     * @throws Throwable rethrows exception if no mapping existent or exceptions raised by implementation
     */
    @Nullable
    public <E extends Throwable> Object handleThrownException(final E exception) throws Throwable {

        final Class<? extends Throwable> exceptionClass = exception.getClass();
        boolean exceptionIsMapped =
            grpcExceptionHandlerMethodResolver.isMethodMappedForException(exceptionClass);
        if (!exceptionIsMapped) {
            throw exception;
        }

        Entry<Object, Method> methodWithInstance =
            grpcExceptionHandlerMethodResolver.resolveMethodWithInstance(exceptionClass);
        Method mappedMethod = methodWithInstance.getValue();
        Object instanceOfMappedMethod = methodWithInstance.getKey();
        Object[] instancedParams = determineInstancedParameters(mappedMethod, exception);

        return invokeMappedMethodSafely(mappedMethod, instanceOfMappedMethod, instancedParams);
    }

    private <E extends Throwable> Object[] determineInstancedParameters(Method mappedMethod, E exception) {

        Parameter[] parameters = mappedMethod.getParameters();
        Object[] instancedParams = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            Class<?> parameterClass = convertToClass(parameters[i]);
            if (parameterClass.isAssignableFrom(exception.getClass())) {
                instancedParams[i] = exception;
                break;
            }
        }
        return instancedParams;
    }

    private Class<?> convertToClass(Parameter parameter) {
        Type paramType = parameter.getParameterizedType();
        if (paramType instanceof Class) {
            return (Class<?>) paramType;
        }
        throw new IllegalStateException("Parametertype of method has to be from Class, it was: " + paramType);
    }

    private Object invokeMappedMethodSafely(
        Method mappedMethod,
        Object instanceOfMappedMethod,
        Object[] instancedParams
    ) throws Throwable {
        Object statusThrowable = null;
        try {
            statusThrowable = mappedMethod.invoke(instanceOfMappedMethod, instancedParams);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw e.getCause(); // throw the exception thrown by implementation
        }
        return statusThrowable;
    }

}
