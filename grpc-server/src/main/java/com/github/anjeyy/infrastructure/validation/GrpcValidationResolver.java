package com.github.anjeyy.infrastructure.validation;

import com.google.protobuf.MessageLiteOrBuilder;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Resolving all classes implementing {@link GrpcConstraintValidator} and marked with annotation
 * {@link GrpcConstraint @GrpcConstraint}. Resolved classes are validation classes for gRPC requests to be validated.
 * <p>
 * The Validation is done via {@link RequestValidationInterceptor}.
 * There can be more than one validation class for the same request type, all of them are being resolved and used for
 * validation.
 *
 * @author Andjelko Perisic (andjelko.perisic@gmail.com)
 * @see GrpcConstraintValidator
 * @see RequestValidationInterceptor
 */
@Slf4j
public class GrpcValidationResolver implements InitializingBean, ApplicationContextAware {

    private Map<String, GrpcConstraintValidator<MessageLiteOrBuilder>> validatorMap;
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        validatorMap = applicationContext.getBeansWithAnnotation(GrpcConstraint.class)
                                         .entrySet()
                                         .stream()
                                         .collect(Collectors.toMap(Entry::getKey, this::convertSafely));
        log.debug("Found {} gRPC validators", validatorMap.size());
    }


    private GrpcConstraintValidator<MessageLiteOrBuilder> convertSafely(Map.Entry<String, Object> entry) {

        Object annotatedValidator = entry.getValue();
        if (annotatedValidator instanceof GrpcConstraintValidator) {
            @SuppressWarnings("unchecked")
            GrpcConstraintValidator<MessageLiteOrBuilder> safeConstraintInstance =
                (GrpcConstraintValidator<MessageLiteOrBuilder>) annotatedValidator;
            return safeConstraintInstance;
        }

        throw new IllegalStateException(
            String.format("@GrpcConstraint annotated class [%s] has to implement GrpcConstraintValidator.class",
                annotatedValidator.getClass())
        );
    }

    /**
     * Retrieve all {@link GrpcConstraintValidator} which are the same class or at least a superclass of given input
     * parameter.
     *
     * @param request gRPC request
     * @param <E>     type of the gRPC request message
     * @return validators to be used in conjunction with the request
     */
    public <E> List<GrpcConstraintValidator<MessageLiteOrBuilder>> findValidators(E request) {
        return validatorMap.values()
                           .stream()
                           .filter(cs -> checkForGenericTypeArgument(cs, request))
                           .collect(Collectors.toList());
    }

    private <E> boolean checkForGenericTypeArgument(
        GrpcConstraintValidator<MessageLiteOrBuilder> grpcConstraintValidator, E request) {

        List<Type> genericTypes = Arrays.asList(grpcConstraintValidator.getClass().getGenericInterfaces());

        return genericTypes.stream()
                           .map(t -> (ParameterizedType) t)
                           .flatMap(pt -> Arrays.stream(pt.getActualTypeArguments()))
                           .map(t -> (Class<?>) t)
                           .anyMatch(c -> c.isAssignableFrom(request.getClass()));
    }

}
