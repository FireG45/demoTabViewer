package ru.fireg45.demotabviewer.requests;

import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import ru.fireg45.demotabviewer.constraints.UniqueEmailConstraint;
import ru.fireg45.demotabviewer.constraints.UniqueUsernameConstraint;

@Getter
@Builder
public class RegistrationRequest {
    @Pattern(regexp = "^[a-zA-Z0-9_-]{3,15}$", message = "Логин должен иметь длину от 3 до 15 символов и " +
            "состоять только из заглавных и строчных латинских букв, цифр, а также символов _ и -!")
    @UniqueUsernameConstraint
    private String username;

    @UniqueEmailConstraint
    @Pattern(regexp = "[^@]+@[^@]+\\.[^@.]+", message = "Введен неверный email!")
    private String email;

    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,30}$",
            message = "Пароль должен содержать хотя бы одну заглавную букву, " +
                    "один специальный символ, состоять только из латинских символов и иметь длинну от 8 до 30 символов!")
    private String password;
}
