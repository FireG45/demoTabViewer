package ru.fireg45.demotabviewer.responses;

import org.springframework.http.HttpStatus;

public class FileUploadResponse {
    private int id;
    HttpStatus status;

    public FileUploadResponse(int id, HttpStatus status) {
        this.id = id;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
