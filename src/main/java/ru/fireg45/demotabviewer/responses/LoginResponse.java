package ru.fireg45.demotabviewer.responses;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class LoginResponse {
    private final String accessToken;
    private final HttpStatus status;

    public LoginResponse(String accessToken, HttpStatus status) {
        this.accessToken = accessToken;
        this.status = status;
    }
}
