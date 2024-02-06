package ru.fireg45.demotabviewer.repositories;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.fireg45.demotabviewer.model.Tabulature;

import java.util.List;

@Repository
public interface TabulaturesRepository extends JpaRepository<Tabulature, Integer> {

    @Query("select avg(r.value) from Review r where r.tab.id = ?1")
    int getAverageRating(int id);

    List<Tabulature> findAllByAuthor(String author, PageRequest pageRequest);
}
