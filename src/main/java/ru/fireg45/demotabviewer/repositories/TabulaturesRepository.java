package ru.fireg45.demotabviewer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.fireg45.demotabviewer.model.Tabulature;

import java.util.List;

@Repository
public interface TabulaturesRepository extends JpaRepository<Tabulature, Integer> {
    public List<Tabulature> findAllByAuthorStartsWithOrTitleStartsWith(String author, String title);
}
