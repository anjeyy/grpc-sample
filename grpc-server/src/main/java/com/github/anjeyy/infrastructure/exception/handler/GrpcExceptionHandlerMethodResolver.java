package com.github.anjeyy.infrastructure.exception.handler;

import com.github.anjeyy.infrastructure.annotation.GrpcAdvice;
import com.github.anjeyy.infrastructure.annotation.GrpcExceptionHandler;
import java.lang.reflect.Method;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

/**
 * Given an annotated {@link GrpcAdvice @GrpcAdvice} class and annotated methods with
 * {@link GrpcExceptionHandler @GrpcExceptionHandler}, {@link GrpcExceptionHandlerMethodResolver} resolves given
 * exception type and maps it to the corresponding method to be executed, when this exception is being raised.
 * <p>
 * For an example how to make use of it, please have a look at {@link GrpcExceptionHandler @GrpcExceptionHandler}.
 * <p>
 *
 * @author Andjelko Perisic (andjelko.perisic@gmail.com)
 * @see GrpcAdvice
 * @see GrpcExceptionHandler
 * @see GrpcAdviceExceptionHandler
 */
@Slf4j
public class GrpcExceptionHandlerMethodResolver implements InitializingBean {

    private final Map<Class<? extends Throwable>, Method> mappedMethods = new HashMap<>();

    private final GrpcAdviceDiscoverer grpcAdviceDiscoverer;

    private Class<? extends Throwable>[] annotatedExceptions;

    public GrpcExceptionHandlerMethodResolver(final GrpcAdviceDiscoverer grpcAdviceDiscoverer) {
        this.grpcAdviceDiscoverer = grpcAdviceDiscoverer;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        grpcAdviceDiscoverer.getAnnotatedMethods()
                            .forEach(this::extractAndMapExceptionToMethod);
    }

    private void extractAndMapExceptionToMethod(Method method) {

        GrpcExceptionHandler annotation = method.getDeclaredAnnotation(GrpcExceptionHandler.class);
        Assert.notNull(annotation, "@GrpcExceptionHandler annotation not found.");
        annotatedExceptions = annotation.value();

        checkForPresentExceptionToMap(method);
        Set<Class<? extends Throwable>> exceptionsToMap = extractExceptions(method.getParameterTypes());
        exceptionsToMap.forEach(exceptionType -> addExceptionMapping(exceptionType, method));
    }

    private void checkForPresentExceptionToMap(Method method) {
        if (method.getParameterTypes().length == 0 && annotatedExceptions.length == 0) {
            throw new IllegalStateException(
                String.format("@GrpcExceptionHandler annotated method [%s] has no mapped exception!",
                    method.getName()));
        }
    }

    private Set<Class<? extends Throwable>> extractExceptions(Class<?>[] methodParamTypes) {

        Set<Class<? extends Throwable>> exceptionsToBeMapped = new HashSet<>();
        for (Class<? extends Throwable> annoClass : annotatedExceptions) {
            boolean annoTypeIsNotSuperclass = Arrays.stream(methodParamTypes).noneMatch(annoClass::isAssignableFrom);
            if (annoTypeIsNotSuperclass) {
                throw new IllegalStateException(
                    String.format(
                        "@GrpcExceptionHandler annotated method declared exception [%s] "
                            + "is NOT superclass of listed parameter arguments [%s]",
                        annoClass, Arrays.toString(methodParamTypes)));
            }
            exceptionsToBeMapped.add(annoClass);
        }

        addMappingInCaseAnnotationIsEmpty(methodParamTypes, exceptionsToBeMapped);
        return exceptionsToBeMapped;
    }

    private void addMappingInCaseAnnotationIsEmpty(
        Class<?>[] methodParamTypes,
        Set<Class<? extends Throwable>> exceptionsToBeMapped
    ) {

        @SuppressWarnings("unchecked")
        Function<Class<?>, Class<? extends Throwable>> convertSafely = clazz -> (Class<? extends Throwable>) clazz;

        Arrays.stream(methodParamTypes)
              .filter(param -> exceptionsToBeMapped.isEmpty())
              .filter(Throwable.class::isAssignableFrom)
              .map(convertSafely) // safe to call, since check for Throwable superclass
              .forEach(exceptionsToBeMapped::add);
    }

    private void addExceptionMapping(Class<? extends Throwable> exceptionType, Method method) {

        Method oldMethod = mappedMethods.put(exceptionType, method);
        if (oldMethod != null && !oldMethod.equals(method)) {
            throw new IllegalStateException("Ambiguous @GrpcExceptionHandler method mapped for [" +
                exceptionType + "]: {" + oldMethod + ", " + method + "}");
        }
    }

    @NonNull
    public <E extends Throwable> Map.Entry<Object, Method> resolveMethodWithInstance(Class<E> exceptionType) {

        Method value = extractExtendedThrowable(exceptionType);
        if (value == null) {
            return new SimpleImmutableEntry<>(null, null);
        }

        Class<?> methodClass = value.getDeclaringClass();
        Object key = grpcAdviceDiscoverer.getAnnotatedBeans()
                                         .values()
                                         .stream()
                                         .filter(obj -> methodClass.isAssignableFrom(obj.getClass()))
                                         .findFirst()
                                         .orElse(null);
        return new SimpleImmutableEntry<>(key, value);
    }

    public <E extends Throwable> boolean isMethodMappedForException(Class<E> exception) {
        return extractExtendedThrowable(exception) != null;
    }

    private <E extends Throwable> Method extractExtendedThrowable(Class<E> exception) {
        return mappedMethods.keySet()
                            .stream().filter(clazz -> clazz.isAssignableFrom(exception))
                            .findAny()
                            .map(mappedMethods::get)
                            .orElse(null);
    }

}
