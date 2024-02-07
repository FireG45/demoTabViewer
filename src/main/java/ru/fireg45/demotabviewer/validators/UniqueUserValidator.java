package ru.fireg45.demotabviewer.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import ru.fireg45.demotabviewer.constraints.UniqueEmailConstraint;
import ru.fireg45.demotabviewer.constraints.UniqueUsernameConstraint;
import ru.fireg45.demotabviewer.services.UserService;

public class UniqueUserValidator implements ConstraintValidator<UniqueUsernameConstraint, String> {

    private final UserService userService;

    @Autowired
    public UniqueUserValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void initialize(UniqueUsernameConstraint usernameConstraint) {
        ConstraintValidator.super.initialize(usernameConstraint);
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        return userService.findByUsername(username).isEmpty();
    }
}
