package ru.fireg45.demotabviewer.requests;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import ru.fireg45.demotabviewer.constraints.UniqueEmailConstraint;
import ru.fireg45.demotabviewer.constraints.UniqueUsernameConstraint;

@Getter
@Setter
public class UserUpdateRequest {
    @Pattern(regexp = "^[a-zA-Z0-9_-]{3,15}$", message = "Логин должен иметь длину от 3 до 15 символов и " +
            "состоять только из заглавных и строчных латинских букв, цифр, а также символов _ и -!")
    @UniqueUsernameConstraint
    public String username;

    @UniqueEmailConstraint
    public String email;
}
