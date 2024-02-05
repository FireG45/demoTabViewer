package ru.fireg45.demotabviewer.responses;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class LoginResponse {
    private final String username;
    private final String accessToken;
    private final HttpStatus status;

    public LoginResponse(String username, String accessToken, HttpStatus status) {
        this.username = username;
        this.accessToken = accessToken;
        this.status = status;
    }
}
