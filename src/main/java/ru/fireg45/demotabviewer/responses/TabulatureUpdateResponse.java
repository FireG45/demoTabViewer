package ru.fireg45.demotabviewer.responses;

public class TabulatureUpdateResponse extends AbstractResponse {
    public String title;
    public String author;

    public TabulatureUpdateResponse(String title, String author) {
        this.title = title;
        this.author = author;
    }
}
