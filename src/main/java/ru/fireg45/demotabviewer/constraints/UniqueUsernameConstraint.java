package ru.fireg45.demotabviewer.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.fireg45.demotabviewer.validators.UniqueEmailValidator;
import ru.fireg45.demotabviewer.validators.UniqueUserValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UniqueUserValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueUsernameConstraint {
    String message() default "Данный логин уже зарегестрирован!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
