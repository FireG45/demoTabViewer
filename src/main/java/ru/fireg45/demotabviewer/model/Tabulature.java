package ru.fireg45.demotabviewer.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;

@Setter
@Getter
@Entity
@Table(name = "tabulature")
public class Tabulature {
    @Id
    @Column(name = "tab_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "author")
    private String author;

    @Column(name = "filepath")
    private String filepath;

    @ManyToOne
    private User user;

    @Column(name = "uploaded")
    @Temporal(TemporalType.TIMESTAMP)
    private Date uploaded;

    public Tabulature() {}

    public Tabulature(String title, String author, String filepath, User user) {
        this.title = title;
        this.author = author;
        this.filepath = filepath;
        this.user = user;
    }

}
