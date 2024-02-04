package ru.fireg45.demotabviewer.responses;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserResponse {
    public String username;

    public UserResponse(String username) {
        this.username = username;
    }
}
