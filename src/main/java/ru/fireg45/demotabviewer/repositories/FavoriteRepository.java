package ru.fireg45.demotabviewer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.fireg45.demotabviewer.model.Favorite;
import ru.fireg45.demotabviewer.model.Tabulature;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {
    List<Favorite> findAllByUserEmail(String username);

    List<Favorite> findAllByTab(Tabulature tabulature);

    @Query("select f from Favorite f where f.user.email = ?1 and f.tab.id = ?2")
    Optional<Favorite> getFavorite(String email, int tabId);
}
