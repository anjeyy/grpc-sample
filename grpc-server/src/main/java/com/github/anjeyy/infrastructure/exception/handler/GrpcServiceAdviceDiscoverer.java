package com.github.anjeyy.infrastructure.exception.handler;

import com.github.anjeyy.infrastructure.annotation.GrpcExceptionHandler;
import com.github.anjeyy.infrastructure.annotation.GrpcServiceAdvice;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * A discovery class to find all Beans annotated with {@link GrpcServiceAdvice @GrpcServiceAdvice} and for all found
 * beans a second search is performed looking for methods with {@link GrpcExceptionHandler @GrpcExceptionHandler}.<br>
 * <br>
 *
 * @author Andjelko Perisic (andjelko.perisic@gmail.com)
 * @see GrpcServiceAdvice
 * @see GrpcExceptionHandler
 */
@Slf4j
public class GrpcServiceAdviceDiscoverer implements InitializingBean, ApplicationContextAware {

    private ApplicationContext applicationContext;
    private Map<String, Object> annotatedBeans;
    private Set<Class<?>> annotatedClasses;
    private Set<Method> annotatedMethods;


    Map<String, Object> getAnnotatedBeans() {
        return annotatedBeans;
    }

    Set<Method> getAnnotatedMethods() {
        return annotatedMethods;
    }


    @Override
    public void afterPropertiesSet() throws Exception {

        annotatedBeans = applicationContext.getBeansWithAnnotation(GrpcServiceAdvice.class);
        annotatedClasses = findAllAnnotatedClasses();
        annotatedMethods = getAnnotatedMethods(annotatedClasses);
    }

    private Set<Class<?>> findAllAnnotatedClasses() {
        return annotatedBeans.values()
                             .stream()
                             .map(Object::getClass)
                             .collect(Collectors.toSet());
    }

    private Set<Method> getAnnotatedMethods(Set<Class<?>> annotatedClasses) {
        Function<Class<?>, Stream<Method>> extractMethodsFromClass = clazz -> Arrays.stream(clazz.getDeclaredMethods());
        return annotatedClasses.stream()
                               .flatMap(extractMethodsFromClass)
                               .filter(method -> method.isAnnotationPresent(GrpcExceptionHandler.class))
                               .collect(Collectors.toSet());
    }

    boolean isAnnotationPresent() {

        return !annotatedClasses.isEmpty() && !annotatedMethods.isEmpty();
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
