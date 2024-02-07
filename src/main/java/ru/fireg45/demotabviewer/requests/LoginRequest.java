package ru.fireg45.demotabviewer.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
@Builder
public class LoginRequest {
    @Pattern(regexp = "[^@]+@[^@]+\\.[^@.]+", message = "Введен неверный email!")
    private String email;

    private String password;
}
