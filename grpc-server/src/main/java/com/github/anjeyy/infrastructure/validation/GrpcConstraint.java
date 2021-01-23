package com.github.anjeyy.infrastructure.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.stereotype.Component;

/**
 * Marker annotation to scan for validation classes, which have to implement {@link GrpcConstraintValidator}.
 * Scanning is done in {@link GrpcValidationResolver}.
 *
 * @author Andjelko Perisic (andjelko.perisic@gmail.com)
 * @see GrpcConstraintValidator
 * @see GrpcValidationResolver
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface GrpcConstraint {

}
