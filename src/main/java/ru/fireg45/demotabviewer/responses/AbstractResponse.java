package ru.fireg45.demotabviewer.responses;

import lombok.Getter;

import java.util.List;

@Getter
public abstract class AbstractResponse {
    protected List<String> errorMessages;

    public AbstractResponse(List<String> errorMessages) {
        this.errorMessages = errorMessages;
    }

    protected AbstractResponse() {
    }
}
