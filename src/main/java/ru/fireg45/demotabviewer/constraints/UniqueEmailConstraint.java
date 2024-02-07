package ru.fireg45.demotabviewer.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.fireg45.demotabviewer.validators.UniqueEmailValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UniqueEmailValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueEmailConstraint {
    String message() default "Данный email уже зарегестрирован!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
