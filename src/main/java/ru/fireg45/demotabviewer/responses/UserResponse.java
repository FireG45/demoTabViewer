package ru.fireg45.demotabviewer.responses;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserResponse {
    public String username;
    public String email;

    public UserResponse(String username) {
        this.username = username;
    }

    public UserResponse(String username, String email) {
        this.username = username;
        this.email = email;
    }
}
