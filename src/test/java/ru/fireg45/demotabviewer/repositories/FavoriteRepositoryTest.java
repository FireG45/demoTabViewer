package ru.fireg45.demotabviewer.repositories;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.fireg45.demotabviewer.model.Favorite;
import ru.fireg45.demotabviewer.model.Tabulature;
import ru.fireg45.demotabviewer.model.User;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class FavoriteRepositoryTest {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TabulaturesRepository tabulaturesRepository;

    private User user1;
    private User user2;

    @BeforeEach
    public void setUp() {
        user1 = userRepository.save(new User("USER1", "EMAIL1", "PASSWORD", "USER"));
        user2 = userRepository.save(new User("USER2", "EMAIL2", "PASSWORD", "USER"));
    }

    @Test
    void getFavorite() {
        Tabulature tabulature = tabulaturesRepository.save(new Tabulature("", "", "",
                user1, 0));

        favoriteRepository.save(new Favorite(user1, tabulature));

        Assertions.assertTrue(favoriteRepository.getFavorite(user1.getEmail(), tabulature.getId()).isPresent());
        Assertions.assertTrue(favoriteRepository.getFavorite(user2.getEmail(), tabulature.getId()).isEmpty());
    }

}