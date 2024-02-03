package ru.fireg45.demotabviewer.responses;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class FileUploadResponse {
    private int id;
    HttpStatus status;

    public FileUploadResponse(int id, HttpStatus status) {
        this.id = id;
        this.status = status;
    }

}
