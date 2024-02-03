package ru.fireg45.demotabviewer.responses;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LoginResponse {
    private final String accessToken;
}
