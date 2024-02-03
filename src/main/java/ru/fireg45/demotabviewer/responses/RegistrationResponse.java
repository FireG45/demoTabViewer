package ru.fireg45.demotabviewer.responses;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
public class RegistrationResponse {
    HttpStatus status;

    public RegistrationResponse(HttpStatus status) {
        this.status = status;
    }
}
