package ru.fireg45.demotabviewer.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.fireg45.demotabviewer.model.Favorite;
import ru.fireg45.demotabviewer.model.Tabulature;
import ru.fireg45.demotabviewer.model.User;
import ru.fireg45.demotabviewer.repositories.FavoriteRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {

    @Mock
    private FavoriteRepository favoriteRepository;

    @InjectMocks
    private FavoriteService favoriteService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void getFavorite_ReturnsFavoriteIfExists() {
        // Arrange
        User user = new User("testuser", "test@example.com", "password", "user");
        Tabulature tab = new Tabulature("Title", "Author", "example/filepath", user, 0);
        Favorite favorite = new Favorite(user, tab);
        when(favoriteRepository.getFavorite(user.getEmail(), tab.getId())).thenReturn(Optional.of(favorite));

        // Act
        Optional<Favorite> result = favoriteService.getFavorite(user.getEmail(), tab.getId());

        // Assert
        assertTrue(result.isPresent());
        assertEquals(favorite, result.get());
    }

    @Test
    void getFavorite_ReturnsEmptyOptionalIfNotFound() {
        // Arrange
        User user = new User("testuser", "test@example.com", "password", "user");
        Tabulature tab = new Tabulature("Title", "Author", "example/filepath", user, 0);
        when(favoriteRepository.getFavorite(user.getEmail(), tab.getId())).thenReturn(Optional.empty());

        // Act
        Optional<Favorite> result = favoriteService.getFavorite(user.getEmail(), tab.getId());

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void save_CallsSaveMethodInRepository() {
        // Arrange
        User user = new User("testuser", "test@example.com", "password", "user");
        Tabulature tab = new Tabulature("Title", "Author", "example/filepath", user, 0);
        Favorite favorite = new Favorite(user, tab);

        // Act
        favoriteService.save(favorite);

        // Assert
        verify(favoriteRepository, times(1)).save(favorite);
    }

    @Test
    void delete_CallsDeleteMethodInRepository() {
        // Arrange
        User user = new User("testuser", "test@example.com", "password", "user");
        Tabulature tab = new Tabulature("Title", "Author", "example/filepath", user, 0);
        Favorite favorite = new Favorite(user, tab);

        // Act
        favoriteService.delete(favorite);

        // Assert
        verify(favoriteRepository, times(1)).delete(favorite);
    }
}
