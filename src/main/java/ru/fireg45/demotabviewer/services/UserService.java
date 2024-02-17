package ru.fireg45.demotabviewer.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.fireg45.demotabviewer.model.Review;
import ru.fireg45.demotabviewer.model.User;
import ru.fireg45.demotabviewer.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final TabulatureService tabulatureService;

    @Autowired
    public UserService(UserRepository userRepository, TabulatureService tabulatureService) {
        this.userRepository = userRepository;
        this.tabulatureService = tabulatureService;
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    public void delete(User user) {
        List<Review> revs = user.getReviews();
        revs.forEach(r -> {
            Integer rating = tabulatureService.getAverageRatingWithOutOne(r.getTab().getId(),
                    r.getId());
            r.getTab().setRating(rating != null ? rating : 0);
        });
        userRepository.delete(user);
    }
}
