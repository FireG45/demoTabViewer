package ru.fireg45.demotabviewer.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.fireg45.demotabviewer.model.Review;
import ru.fireg45.demotabviewer.model.Tabulature;
import ru.fireg45.demotabviewer.model.User;
import ru.fireg45.demotabviewer.requests.ReviewRequest;
import ru.fireg45.demotabviewer.security.UserPrincipal;
import ru.fireg45.demotabviewer.services.ReviewService;
import ru.fireg45.demotabviewer.services.TabulatureService;
import ru.fireg45.demotabviewer.services.UserService;

import java.util.Optional;

@RestController
@CrossOrigin
public class ReviewController {
    private final UserService userService;
    private final TabulatureService tabulatureService;
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(UserService userService, TabulatureService tabulatureService, ReviewService reviewService) {
        this.userService = userService;
        this.tabulatureService = tabulatureService;
        this.reviewService = reviewService;
    }

    @PostMapping("/addreview")
    public ResponseEntity<String> addReview(@RequestBody ReviewRequest reviewRequest,
                                            @AuthenticationPrincipal UserPrincipal principal) {
        Optional<Tabulature> tab = tabulatureService.findById(reviewRequest.getTabId());
        Optional<User> user = userService.findByEmail(principal.getEmail());
        if (tab.isEmpty() || user.isEmpty()) return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
        Optional<Review> review = reviewService.findReview(user.get(), tab.get());
        if (review.isEmpty()) {
            reviewService.save(new Review(tab.get(), user.get(), reviewRequest.getValue()));
        } else {
            Review updatedReview = review.get();
            updatedReview.setValue(reviewRequest.getValue());
            reviewService.save(updatedReview);
        }
        Tabulature updatedTab = tab.get();
        updatedTab.setRating(tabulatureService.getAverageRating(updatedTab.getId()));
        tabulatureService.save(updatedTab);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}
