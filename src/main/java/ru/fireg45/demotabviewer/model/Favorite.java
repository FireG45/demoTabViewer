package ru.fireg45.demotabviewer.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "favorite")
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favorite_id")
    private int id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Tabulature tab;

    public Favorite(User user, Tabulature tab) {
        this.user = user;
        this.tab = tab;
    }

    public Favorite() {
    }
}
