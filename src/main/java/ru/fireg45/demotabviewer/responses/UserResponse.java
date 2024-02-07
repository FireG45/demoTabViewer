package ru.fireg45.demotabviewer.responses;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class UserResponse extends AbstractResponse {
    public String username;
    public String email;

    public UserResponse(String username) {
        this.username = username;
    }

    public UserResponse(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public UserResponse(List<String> errorMessages, String username, String email) {
        super(errorMessages);
        this.username = username;
        this.email = email;
    }

    public UserResponse(List<String> errorList) {
        super(errorList);
        username = "";
        email = "";
    }
}
