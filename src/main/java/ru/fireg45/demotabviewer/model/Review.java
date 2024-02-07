package ru.fireg45.demotabviewer.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
@Entity
@Getter
@Setter
@Table(name = "review")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private int reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Tabulature tab;

    @ManyToOne(fetch = FetchType.LAZY)
    private User uploaded;

    private int value;

    public Review(Tabulature tabulature_id, User uploaded_by, int value) {
        this.tab = tabulature_id;
        this.uploaded = uploaded_by;
        this.value = value;
    }

    public Review() {}
}
