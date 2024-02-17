package ru.fireg45.demotabviewer.repositories;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.fireg45.demotabviewer.model.Review;
import ru.fireg45.demotabviewer.model.Tabulature;
import ru.fireg45.demotabviewer.model.User;


@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TabulaturesRepository tabulaturesRepository;

    private User user;

    @BeforeEach
    public void setUp() {
        user = userRepository.save(new User("USER", "EMAIL", "PASSWORD", "USER"));
    }

    @Test
    void getRatingByUserId() {
        Tabulature tabulature = tabulaturesRepository.save(new Tabulature("TITLE", "AUTHOR", "FILE",
                user, 0));

        reviewRepository.save(new Review(tabulature, user, 5));

        Assertions.assertEquals(5, reviewRepository.getRatingByUserId(user.getId(), tabulature.getId()));
    }

}
