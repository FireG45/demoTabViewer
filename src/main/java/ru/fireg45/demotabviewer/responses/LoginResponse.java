package ru.fireg45.demotabviewer.responses;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
public class LoginResponse extends AbstractResponse{
    private final String username;
    private final String accessToken;
    private final HttpStatus status;

    public LoginResponse(List<String> errors, String username, String accessToken, HttpStatus status) {
        super(errors);
        this.username = username;
        this.accessToken = accessToken;
        this.status = status;
    }

    public LoginResponse(String username, String accessToken, HttpStatus status) {
        this.username = username;
        this.accessToken = accessToken;
        this.status = status;
    }

    public LoginResponse(List<String> errors) {
        super(errors);
        this.username = "";
        this.accessToken = "";
        this.status = HttpStatus.OK;
    }
}
