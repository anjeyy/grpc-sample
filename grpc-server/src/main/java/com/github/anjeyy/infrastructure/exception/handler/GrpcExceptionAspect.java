package com.github.anjeyy.infrastructure.exception.handler;

import com.github.anjeyy.infrastructure.annotation.GrpcServiceAdvice;
import io.grpc.stub.StreamObserver;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

/**
 * Exception handling for thrown {@link RuntimeException} inside annotated CLasses with {@link
 * net.devh.boot.grpc.server.service.GrpcService @GrpcService}, which implement {@link io.grpc.BindableService}.
 * <p>
 * After calling the mapped Methods to the corresponding Exception, the returned {@link Throwable} is beeing send to the
 * {@link io.grpc.stub.StreamObserver#onError(Throwable)}.
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
@ConditionalOnBean(annotation = GrpcServiceAdvice.class)
public class GrpcExceptionAspect {

    private final GrpcExceptionHandlerMethodResolver exceptionHandlerMethodResolver;

    private Throwable exception;
    private Method mappedMethod;
    private Object instanceOfMappedMethod;


    @Pointcut("within(@net.devh.boot.grpc.server.service.GrpcService *)")
    void grpcServiceAnnotatedPointcut() {
    }

    @Pointcut("within(io.grpc.BindableService+)")
    void implementedBindableServicePointcut() {
    }

    @AfterThrowing(
        pointcut = "grpcServiceAnnotatedPointcut() && implementedBindableServicePointcut()",
        throwing = "exception")
    public <E extends Throwable> void handleExceptionInsideGrpcService(JoinPoint joinPoint, E exception) {
        log.error("Runtimeexception caught during gRPC service execution: ", exception);
        this.exception = exception;

        extractNecessaryInformation();
        Throwable throwable = invokeMappedMethodSafely();
        closeStreamObserverOnError(joinPoint.getArgs(), throwable);
    }

    private void extractNecessaryInformation() {

        final Class<? extends Throwable> exceptionClass = exception.getClass();

        Map.Entry<Object, Method> methodWithInstance =
            exceptionHandlerMethodResolver.resolveMethodWithInstance(exceptionClass);
        mappedMethod =
            Optional.of(methodWithInstance)
                    .map(Entry::getValue)
                    .orElseThrow(
                        () -> new IllegalStateException("No mapped method found for Exception " + exceptionClass));
        instanceOfMappedMethod =
            Optional.of(methodWithInstance)
                    .map(Entry::getKey)
                    .orElseThrow(
                        () -> new IllegalStateException(" No mapped instance found for Exception " + exceptionClass));
    }

    private Throwable invokeMappedMethodSafely() {
        try {
            Object[] instancedParams = determineInstancedParameters(mappedMethod);
            Object statusThrowable = mappedMethod.invoke(instanceOfMappedMethod, instancedParams);
            return castToThrowable(statusThrowable);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new IllegalStateException("No mapped instance found for Exception " + exception);
        }
    }

    private Object[] determineInstancedParameters(Method mappedMethod) {

        Parameter[] parameters = mappedMethod.getParameters();
        Object[] instancedParams = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            Class<?> parameterClass = convertToClass(parameters[i]);
            if (parameterClass.equals(exception.getClass())) {
                instancedParams[i] = exception;
                break;
            }
        }
        return instancedParams;
    }

    private Class<?> convertToClass(Parameter parameter) {
        return Optional.of(parameter)
                       .map(Parameter::getParameterizedType)
                       .filter(type -> type instanceof Class)
                       .map(type -> (Class<?>) type)
                       .orElseThrow();
    }

    private Throwable castToThrowable(Object statusThrowable) {
        return Optional.of(statusThrowable)
                       .filter(thrbl -> thrbl instanceof Throwable)
                       .map(thrbl -> (Throwable) thrbl)
                       .orElseThrow(() -> new IllegalStateException(
                           "Return type has to beo f type java.lang.Throable: " + statusThrowable));
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
