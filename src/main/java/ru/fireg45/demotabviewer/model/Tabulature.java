package ru.fireg45.demotabviewer.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "tabulature")
public class Tabulature {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "author")
    private String author;

    @Column(name = "filepath")
    private String filepath;

    public Tabulature() {}

    public Tabulature(String title, String author, String filepath) {
        this.title = title;
        this.author = author;
        this.filepath = filepath;
    }

}
