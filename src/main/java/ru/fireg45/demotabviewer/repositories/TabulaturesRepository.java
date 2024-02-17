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
    Integer getAverageRating(int id);

    List<Tabulature> findAllByAuthor(String author, PageRequest pageRequest);

    @Query("select avg(r.value) from Review r where r.tab.id = ?1 and r.id != ?2")
    Integer getAverageRating(int id, int reviewId);

    @Query("select t from Tabulature t where t in (select f.tab from Favorite f where f.user.email = ?1)")
    List<Tabulature> findFavoritesByEmail(String email);

    @Query("select count(*) from Favorite f where f.tab.id = ?1 and f.user.email = ?2")
    Integer countFavorites(int tabId, String email);
}
