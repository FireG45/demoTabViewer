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
