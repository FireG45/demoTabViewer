package ru.fireg45.demotabviewer.responses;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
public class FileUploadResponse extends AbstractResponse {
    private int id;
    HttpStatus status;

    public FileUploadResponse(int id) {
        this.id = id;
    }

    public FileUploadResponse(List<String> errorMessages) {
        super(errorMessages);
    }
}
