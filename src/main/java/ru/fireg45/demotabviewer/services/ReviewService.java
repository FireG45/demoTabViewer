package ru.fireg45.demotabviewer.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.fireg45.demotabviewer.model.Review;
import ru.fireg45.demotabviewer.model.Tabulature;
import ru.fireg45.demotabviewer.model.User;
import ru.fireg45.demotabviewer.repositories.ReviewRepository;

import java.util.Optional;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public int getAverageRating(int id) {
        return 0;
    }

    public int getRatingCount(int id) {
        return 0;
    }

    public void save(Review review) {
        reviewRepository.save(review);
    }

    public Integer getRatingByUserAndTab(int id, int tabId) {
        return reviewRepository.getRatingByUserId(id, tabId);
    }

    public Optional<Review> findReview(User user, Tabulature tabulature) {
        return reviewRepository.getReviewByUploadedAndTab(user, tabulature);
    }

}
