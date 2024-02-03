package ru.fireg45.demotabviewer.requests;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RegistrationRequest {
    private String username;
    private String email;
    private String password;
}
