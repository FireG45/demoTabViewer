package ru.fireg45.demotabviewer.responses;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
public class RegistrationResponse extends AbstractResponse{
    HttpStatus status;

    public RegistrationResponse(List<String> errorMessages, HttpStatus status) {
        super(errorMessages);
        this.status = status;
    }

    public RegistrationResponse(HttpStatus status) {
        this.status = status;
    }

    public RegistrationResponse(List<String> errorList) {
        this.errorMessages = errorList;
        this.status = HttpStatus.OK;
    }
}
