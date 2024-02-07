package ru.fireg45.demotabviewer.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import ru.fireg45.demotabviewer.constraints.UniqueEmailConstraint;
import ru.fireg45.demotabviewer.services.UserService;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmailConstraint, String> {

    private final UserService userService;

    @Autowired
    public UniqueEmailValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void initialize(UniqueEmailConstraint emailConstraint) {
        ConstraintValidator.super.initialize(emailConstraint);
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        return userService.findByEmail(email).isEmpty();
    }
}
