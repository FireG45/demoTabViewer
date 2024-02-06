package ru.fireg45.demotabviewer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.fireg45.demotabviewer.model.Review;
import ru.fireg45.demotabviewer.model.Tabulature;
import ru.fireg45.demotabviewer.model.User;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    @Query("select r.value from Review r where r.uploaded.id = ?1 and r.tab.id = ?2")
    Integer getRatingByUserId(int id, int tabId);

    Optional<Review> getReviewByUploadedAndTab(User uploaded_by, Tabulature tab);
}
