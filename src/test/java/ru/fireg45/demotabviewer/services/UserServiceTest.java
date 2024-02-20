package ru.fireg45.demotabviewer.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.fireg45.demotabviewer.model.Tabulature;
import ru.fireg45.demotabviewer.model.User;
import ru.fireg45.demotabviewer.repositories.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    void findByEmail() {
        Mockito.when(userRepository.findUserByEmail("EMAIL"))
                .thenReturn(Optional.of(new User("USER", "EMAIL", "PASSWORD", "USER")));

        Assertions.assertTrue(userService.findByEmail("EMAIL").isPresent());
        Assertions.assertEquals("EMAIL", userService.findByEmail("EMAIL").get().getEmail());
    }

    @Test
    void save() {
        User user = new User("USER", "EMAIL", "PASSWORD", "USER");
        User saved = new User("USER", "EMAIL", "PASSWORD", "USER");
        saved.setId(1);
        Mockito.when(userRepository.save(user))
                .thenReturn(saved);

        Assertions.assertNotNull(userService.save(user));
        Assertions.assertEquals(userService.save(user).getId(), 1);
    }

    @Test
    void findByUsername() {
        Mockito.when(userRepository.findUserByUsername("USER"))
                .thenReturn(Optional.of(new User("USER", "EMAIL", "PASSWORD", "USER")));

        Assertions.assertTrue(userService.findByUsername("USER").isPresent());
        Assertions.assertEquals("USER", userService.findByUsername("USER").get().getUsername());
    }

}