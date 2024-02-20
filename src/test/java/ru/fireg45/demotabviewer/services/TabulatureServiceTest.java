package ru.fireg45.demotabviewer.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.fireg45.demotabviewer.model.Tabulature;
import ru.fireg45.demotabviewer.model.User;
import ru.fireg45.demotabviewer.repositories.TabulaturesRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class TabulatureServiceTest {

    @Mock
    private TabulaturesRepository tabulaturesRepository;

    @InjectMocks
    private TabulatureService tabulatureService;

    @Test
    void findById() {
        User user = new User("USER", "EMAIL", "PASSWORD", "USER");
        Tabulature tab = new Tabulature("", "", "", user, 0);
        tab.setId(1);
        Mockito.when(tabulaturesRepository.findById(1))
                .thenReturn(Optional.of(tab));

        Optional<Tabulature> got = tabulatureService.findById(1);
        Assertions.assertTrue(got.isPresent());
        Assertions.assertEquals(got.get().getId(), 1);
    }

    @Test
    void findAll() {
        User user = new User("USER", "EMAIL", "PASSWORD", "USER");
        List<Tabulature> tabulatures = List.of(
                new Tabulature("TITLE1", "AUTHOR1", "file1.gp5", user, 1),
                new Tabulature("TITLE2", "AUTHOR2", "file2.gp5", user, 2),
                new Tabulature("TITLE3", "AUTHOR3", "file3.gp5", user, 3)
        );
        Mockito.when(tabulaturesRepository.findAll())
                .thenReturn(tabulatures);

        Assertions.assertEquals(3, tabulatureService.findAll().size());
    }

    @Test
    void findAllPageRequest() {
        User user = new User("USER", "EMAIL", "PASSWORD", "USER");
        List<Tabulature> tabulatures = List.of(
                new Tabulature("TITLE1", "AUTHOR1", "file1.gp5", user, 1),
                new Tabulature("TITLE2", "AUTHOR2", "file2.gp5", user, 2),
                new Tabulature("TITLE3", "AUTHOR3", "file3.gp5", user, 3)
        );

        Mockito.when(tabulaturesRepository.findAll(PageRequest.of(1, 3)))
                .thenReturn(new PageImpl<>(tabulatures));

        Assertions.assertEquals(3, tabulatureService.findAll(PageRequest.of(1, 3)).size());
    }

    @Test
    void save() {
        User user = new User("", "", "", "");
        Tabulature tab = new Tabulature("", "", "", user, 0);
        Tabulature savedTab = new Tabulature("", "", "", user, 0);
        savedTab.setId(1);

        Mockito.when(tabulaturesRepository.save(tab))
                .thenReturn(tab);

        Assertions.assertNotNull(tabulatureService.save(tab));
    }


    @Test
    void findAllByAuthor() {
        User user = new User("USER", "EMAIL", "PASSWORD", "USER");
        List<Tabulature> tabulatures = List.of(
                new Tabulature("TITLE1", "AUTHOR1", "file1.gp5", user, 1),
                new Tabulature("TITLE2", "AUTHOR2", "file2.gp5", user, 2),
                new Tabulature("TITLE3", "AUTHOR3", "file3.gp5", user, 3)
        );

        Mockito.when(tabulaturesRepository.findAllByAuthor("AUTHOR1", PageRequest.of(1, 3)))
                .thenReturn(tabulatures.stream().filter(t -> Objects.equals(t.getAuthor(), "AUTHOR1")).toList());

        List<Tabulature> tabs = tabulatureService.findAllByAuthor("AUTHOR1", PageRequest.of(1, 3));
        Assertions.assertEquals(1, tabs.size());
        Assertions.assertEquals("AUTHOR1", tabs.get(0).getAuthor());
    }

}