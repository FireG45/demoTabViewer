package ru.fireg45.demotabviewer.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.fireg45.demotabviewer.model.Review;
import ru.fireg45.demotabviewer.model.Tabulature;
import ru.fireg45.demotabviewer.model.User;
import ru.fireg45.demotabviewer.repositories.ReviewRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewService reviewService;

    @Test
    void save_ReviewSavedSuccessfully() {
        Review review = new Review();
        when(reviewRepository.save(review)).thenReturn(review);

        reviewService.save(review);

        verify(reviewRepository, times(1)).save(review);
    }

    @Test
    void getRatingByUserAndTab_ReturnsRating() {
        int userId = 1;
        int tabId = 1;
        Integer expectedRating = 4;
        when(reviewRepository.getRatingByUserId(userId, tabId)).thenReturn(expectedRating);

        Integer result = reviewService.getRatingByUserAndTab(userId, tabId);

        assertEquals(expectedRating, result);
    }

    @Test
    void findReview_ReturnsOptionalReview() {
        User user = new User();
        Tabulature tabulature = new Tabulature();
        Optional<Review> expectedReview = Optional.of(new Review());
        when(reviewRepository.getReviewByUploadedAndTab(user, tabulature)).thenReturn(expectedReview);

        Optional<Review> result = reviewService.findReview(user, tabulature);

        assertEquals(expectedReview, result);
    }
}
