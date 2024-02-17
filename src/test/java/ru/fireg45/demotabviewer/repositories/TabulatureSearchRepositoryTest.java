package ru.fireg45.demotabviewer.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.fireg45.demotabviewer.model.Tabulature;
import ru.fireg45.demotabviewer.model.User;
import ru.fireg45.demotabviewer.util.Tuple;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class TabulatureSearchRepositoryTest {

//    @Autowired
//    @PersistenceContext
//    EntityManager entityManager;
//
//    @Autowired
//    private TabulaturesRepository tabulaturesRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private TabulatureSearchRepositoryImpl tabulatureSearchRepository;
//
//    private User user;
//
//    @BeforeEach
//    public void setUp() {
//        user = userRepository.save(new User("", "", "", "USER"));
//    }
//
//    @Test
//    void search() throws InterruptedException {
//        List<Tabulature> tabs = tabulaturesRepository.saveAll(List.of(
//                new Tabulature("TAB 1", "TAB 1", "FILE", user, 0),
//                new Tabulature("TAB 2", "TAB 2", "FILE", user, 0),
//                new Tabulature("BAB", "BAB", "FILE", user, 0),
//                new Tabulature("BAB", "BAB", "FILE", user, 0),
//                new Tabulature("BAB", "BAB", "FILE", user, 0),
//                new Tabulature("BAB", "BAB", "FILE", user, 0),
//                new Tabulature("BAB", "BAB", "FILE", user, 0),
//                new Tabulature("TAB 2", "TAB 2", "FILE", user, 0),
//                new Tabulature("TAB 2", "TAB 2", "FILE", user, 0)
//        ));
//
//        Tuple<List<Tabulature>, Long> result1 = tabulatureSearchRepository.search("TAB", 1,
//                10, 1);
//
//        Tuple<List<Tabulature>, Long> result2 = tabulatureSearchRepository.search("BAB", 1,
//                10, 1);
//
//        Assertions.assertEquals(4, result1.getValue2());
//        Assertions.assertEquals(5, result2.getValue2());
//    }

}
