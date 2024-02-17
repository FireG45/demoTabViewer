package ru.fireg45.demotabviewer.repositories;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.fireg45.demotabviewer.model.Favorite;
import ru.fireg45.demotabviewer.model.Review;
import ru.fireg45.demotabviewer.model.Tabulature;
import ru.fireg45.demotabviewer.model.User;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class TabulatureRepositoryTest {

    @Autowired
    private TabulaturesRepository tabulaturesRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    private User user;

    @BeforeEach
    public void setUp() {
        user = userRepository.save(new User("USER2", "EMAIL2", "PASSWORD", "USER"));
    }

    @Test
    public void save() {
        Tabulature tabulature = new Tabulature("TITLE", "AUTHOR", "file.gp5", user, 0);

        Tabulature saved = tabulaturesRepository.save(tabulature);

        Assertions.assertNotNull(saved);
        Assertions.assertTrue(saved.getId() > 0);
    }

    @Test
    public void findAll() {
        Tabulature tabulature1 = new Tabulature("TITLE1", "AUTHOR1", "file.gp5",
                user, 0);

        Tabulature tabulature2 = new Tabulature("TITLE2", "AUTHOR2", "file.gp5",
                user, 0);

        tabulaturesRepository.save(tabulature1);
        tabulaturesRepository.save(tabulature2);

        List<Tabulature> found = tabulaturesRepository.findAll();

        Assertions.assertEquals(2, found.size());
    }

    @Test
    public void findById() {
        User user = userRepository.save(new User("USER2", "EMAIL2", "PASSWORD", "USER"));

        Tabulature tabulature1 = new Tabulature("TITLE1", "AUTHOR1", "file.gp5",
                user, 0);

        Tabulature saved = tabulaturesRepository.save(tabulature1);

        Tabulature found = tabulaturesRepository.findById(saved.getId()).orElse(null);

        Assertions.assertNotNull(found);
        Assertions.assertEquals(saved.getId(), found.getId());
    }

    @Test
    public void delete() {
        Tabulature tabulature = new Tabulature("TITLE1", "AUTHOR1", "file.gp5",
                user, 0);

        Tabulature saved = tabulaturesRepository.save(tabulature);

        tabulaturesRepository.delete(saved);

        Assertions.assertTrue(tabulaturesRepository.findById(saved.getId()).isEmpty());
    }

    @Test
    void getAverageRating() {
        Tabulature tabulature = new Tabulature("TITLE1", "AUTHOR1", "file.gp5",
                user, 0);

        Tabulature saved = tabulaturesRepository.save(tabulature);

        Review mainReview = new Review(saved, user, 2);

        List<Review> rev = List.of(
                mainReview,
                new Review(saved, user, 3),
                new Review(saved, user, 4),
                new Review(saved, user, 3),
                new Review(saved, user, 1));

        reviewRepository.saveAll(rev);

        Assertions.assertEquals(tabulaturesRepository.getAverageRating(saved.getId()),
                rev.stream().mapToInt(Review::getValue).sum() / rev.size());
    }

    @Test
    void getAverageRating_wo_currentReview() {
        Tabulature tabulature = new Tabulature("TITLE1", "AUTHOR1", "file.gp5",
                user, 0);

        Tabulature saved = tabulaturesRepository.save(tabulature);

        Review mainReview = reviewRepository.save(new Review(saved, user, 2));

        List<Review> rev = List.of(
                new Review(saved, user, 3),
                new Review(saved, user, 4),
                new Review(saved, user, 3),
                new Review(saved, user, 1));

        reviewRepository.saveAll(rev);

        Assertions.assertEquals(tabulaturesRepository.getAverageRating(saved.getId(), mainReview.getId()),
                rev.stream().mapToInt(Review::getValue).sum() / rev.size());
    }

    @Test
    void findAllByAuthor() {
        List<Tabulature> tabs1 = List.of(
                new Tabulature("TITLE1", "AUTHOR1", "file.gp5", user, 0),
                new Tabulature("TITLE1", "AUTHOR1", "file.gp5", user, 0),
                new Tabulature("TITLE1", "AUTHOR1", "file.gp5", user, 0),
                new Tabulature("TITLE1", "AUTHOR1", "file.gp5", user, 0)
        );

        List<Tabulature> tabs2 = List.of(
                new Tabulature("TITLE1", "AUTHOR2", "file.gp5", user, 0),
                new Tabulature("TITLE1", "AUTHOR2", "file.gp5", user, 0),
                new Tabulature("TITLE1", "AUTHOR2", "file.gp5", user, 0)
        );

        tabulaturesRepository.saveAll(tabs1);
        tabulaturesRepository.saveAll(tabs2);

        Assertions.assertEquals(tabulaturesRepository.findAllByAuthor("AUTHOR1", null).size(),
                tabs1.size());

        Assertions.assertEquals(tabulaturesRepository.findAllByAuthor("AUTHOR2", null).size(),
                tabs2.size());
    }

    @Test
    void countFavorites() {
        Tabulature savedTab = tabulaturesRepository.save(new Tabulature("TITLE1", "AUTHOR1",
                "file.gp5", user, 0));

        favoriteRepository.save(new Favorite(user, savedTab));

        Assertions.assertTrue(tabulaturesRepository.countFavorites(savedTab.getId(), user.getEmail()) > 0);
    }

    @Test
    void findFavoritesByEmail() {
        User user2 = userRepository.save(new User("USER2", "DJDJDJ", "PASSWORD", "USER"));

        List<Tabulature> tabs1 = tabulaturesRepository.saveAll(List.of(
                new Tabulature("TITLE1", "AUTHOR1", "file.gp5", user, 0),
                new Tabulature("TITLE1", "AUTHOR1", "file.gp5", user, 0),
                new Tabulature("TITLE1", "AUTHOR1", "file.gp5", user, 0),
                new Tabulature("TITLE1", "AUTHOR1", "file.gp5", user, 0),
                new Tabulature("TITLE1", "AUTHOR1", "file.gp5", user, 0)
        ));

        List<Tabulature> tabs2 = tabulaturesRepository.saveAll(List.of(
                new Tabulature("TITLE1", "AUTHOR1", "file.gp5", user, 0),
                new Tabulature("TITLE1", "AUTHOR1", "file.gp5", user, 0),
                new Tabulature("TITLE1", "AUTHOR1", "file.gp5", user, 0)
        ));

        tabs1.forEach(tabulature -> favoriteRepository.save(new Favorite(user, tabulature)));
        tabs2.forEach(tabulature -> favoriteRepository.save(new Favorite(user2, tabulature)));

        Assertions.assertEquals(tabulaturesRepository.findFavoritesByEmail(user.getEmail()).size(), tabs1.size());
        Assertions.assertEquals(tabulaturesRepository.findFavoritesByEmail(user2.getEmail()).size(), tabs2.size());
    }
}
