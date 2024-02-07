package ru.fireg45.demotabviewer.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequest {
    public String username;
    public String email;
}
