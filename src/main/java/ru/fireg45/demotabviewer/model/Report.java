package ru.fireg45.demotabviewer.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "report")
public class Report {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Tabulature tab;

    @Column(name = "message")
    private String message;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    public Report(Tabulature tab, String message, User user) {
        this.tab = tab;
        this.message = message;
        this.user = user;
    }

    public Report() {}
}
