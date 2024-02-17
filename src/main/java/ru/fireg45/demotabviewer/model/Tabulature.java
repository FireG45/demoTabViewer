package ru.fireg45.demotabviewer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@Entity
@Indexed
@Table(name = "tabulature")
public class Tabulature {
    @Id
    @Column(name = "tab_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @FullTextField
    @Column(name = "title")
    private String title;

    @FullTextField
    @Column(name = "author")
    private String author;

    @Column(name = "filepath")
    private String filepath;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(name = "uploaded")
    @Temporal(TemporalType.TIMESTAMP)
    private Date uploaded;

    @Column(name = "last_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdate;

    @JsonIgnore
    @OneToMany(mappedBy = "tab", cascade = CascadeType.ALL)
    private List<Review> reviews;

    @Column(name = "rating")
    private int rating;

    @JsonIgnore
    @OneToMany(mappedBy = "tab", cascade = CascadeType.ALL)
    private List<Favorite> favorite;

    public Tabulature() {}

    public Tabulature(String title, String author, String filepath, User user, int rating) {
        this.title = title;
        this.author = author;
        this.filepath = filepath;
        this.user = user;
        this.uploaded = new Date();
        this.lastUpdate = new Date();
        this.rating = rating;
    }


}
