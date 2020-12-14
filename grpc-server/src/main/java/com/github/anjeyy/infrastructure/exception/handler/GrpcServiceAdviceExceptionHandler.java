package com.github.anjeyy.infrastructure.exception.handler;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.Map.Entry;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * As part of Spring AOP, when a thrown exception is caught inside annotated class
 * {@link GrpcService @GrpcService}, which implements {@link io.grpc.BindableService},
 * then this thrown exception is being handled. Specifically handled by {@link GrpcExceptionHandlerMethodResolver}
 * where a mapping between exception and the in case to be executed method exists.
 * <p>
 * After calling the mapped Method, the returned {@link Throwable} is being send to the
 * {@link StreamObserver#onError(Throwable)}. In case the return type is {@link Status} a conversion to
 * {@link Status#asRuntimeException()} is made.
 *
 * @author Andjelko Perisic (andjelko.perisic@gmail.com)
 * @see GrpcExceptionHandlerMethodResolver
 */
@Slf4j
@Aspect
public class GrpcServiceAdviceExceptionHandler {

    private final GrpcExceptionHandlerMethodResolver grpcExceptionHandlerMethodResolver;

    private Throwable exception;
    private Method mappedMethod;
    private Object instanceOfMappedMethod;

    public GrpcServiceAdviceExceptionHandler(
        final GrpcExceptionHandlerMethodResolver grpcExceptionHandlerMethodResolver) {
        this.grpcExceptionHandlerMethodResolver = grpcExceptionHandlerMethodResolver;
    }


    @Pointcut("within(@net.devh.boot.grpc.server.service.GrpcService *)")
    void grpcServiceAnnotatedPointcut() {
    }

    @Pointcut("within(io.grpc.BindableService+)")
    void implementedBindableServicePointcut() {
    }

    @AfterThrowing(
        pointcut = "grpcServiceAnnotatedPointcut() && implementedBindableServicePointcut()",
        throwing = "exception")
    public <E extends Throwable> void handleExceptionInsideGrpcService(
        JoinPoint joinPoint, E exception) throws Throwable {

        log.error("Exception caught during gRPC service execution: ", exception);
        this.exception = exception;

        boolean exceptionIsMapped =
            grpcExceptionHandlerMethodResolver.isMethodMappedForException(exception.getClass());
        if (!exceptionIsMapped) {
            return;
        }

        extractNecessaryInformation();
        Throwable throwable = invokeMappedMethodSafely();
        closeStreamObserverOnError(joinPoint.getArgs(), throwable);
    }

    private void extractNecessaryInformation() {

        final Class<? extends Throwable> exceptionClass = exception.getClass();

        Entry<Object, Method> methodWithInstance =
            grpcExceptionHandlerMethodResolver.resolveMethodWithInstance(exceptionClass);
        mappedMethod =
            Optional.of(methodWithInstance)
                    .map(Entry::getValue)
                    .orElseThrow(() -> new IllegalStateException(
                        "No mapped method found for Exception " + exceptionClass));
        instanceOfMappedMethod =
            Optional.of(methodWithInstance)
                    .map(Entry::getKey)
                    .orElseThrow(() -> new IllegalStateException(
                        " No mapped instance found for Exception " + exceptionClass));
    }

    private Throwable invokeMappedMethodSafely() throws Throwable {
        try {
            Object[] instancedParams = determineInstancedParameters(mappedMethod);
            Object statusThrowable = mappedMethod.invoke(instanceOfMappedMethod, instancedParams);
            return castToThrowable(statusThrowable);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw e.getCause();
        }
    }

    private Object[] determineInstancedParameters(Method mappedMethod) {

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

    private Throwable castToThrowable(Object statusThrowable) {

        if (statusThrowable instanceof Status) {
            Status statusToBeWrapped = (Status) statusThrowable;
            return statusToBeWrapped.asRuntimeException();
        }
        return Optional.of(statusThrowable)
                       .filter(thrbl -> thrbl instanceof Throwable)
                       .map(thrbl -> (Throwable) thrbl)
                       .orElseThrow(() -> new IllegalStateException(
                           "Return type has to be of type java.lang.Throwable: " + statusThrowable));
    }

    private void closeStreamObserverOnError(Object[] joinPointParams, Throwable throwable) {
        for (Object param : joinPointParams) {
            if (param instanceof StreamObserver) {
                StreamObserver<?> streamObserver = (StreamObserver<?>) param;
                streamObserver.onError(throwable);
            }
        }
    }

}
