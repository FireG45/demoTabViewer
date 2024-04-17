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
import ru.fireg45.demotabviewer.repositories.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TabulatureService tabulatureService;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        // Setup mock responses or behavior here if needed
    }

    @Test
    void findByEmail_ExistingEmail_ReturnsUser() {
        String email = "test@example.com";
        User expectedUser = new User();
        when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(expectedUser));

        Optional<User> result = userService.findByEmail(email);

        assertTrue(result.isPresent());
        assertEquals(expectedUser, result.get());
    }

    @Test
    void findByEmail_NonExistingEmail_ReturnsEmptyOptional() {
        String email = "test@example.com";
        when(userRepository.findUserByEmail(email)).thenReturn(Optional.empty());

        Optional<User> result = userService.findByEmail(email);

        assertFalse(result.isPresent());
    }

    @Test
    void save_ValidUser_ReturnsSavedUser() {
        User user = new User();
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.save(user);

        assertEquals(user, result);
    }

    @Test
    void findByUsername_ExistingUsername_ReturnsUser() {
        String username = "testUser";
        User expectedUser = new User();
        when(userRepository.findUserByUsername(username)).thenReturn(Optional.of(expectedUser));

        Optional<User> result = userService.findByUsername(username);

        assertTrue(result.isPresent());
        assertEquals(expectedUser, result.get());
    }

    @Test
    void findByUsername_NonExistingUsername_ReturnsEmptyOptional() {
        String username = "testUser";
        when(userRepository.findUserByUsername(username)).thenReturn(Optional.empty());

        Optional<User> result = userService.findByUsername(username);

        assertFalse(result.isPresent());
    }

    @Test
    void delete_UserWithReviews_UpdatesTabRatingsAndDeletesUser() {
        User user = new User();
        Review review1 = new Review();
        review1.setId(1);
        review1.setTab(new Tabulature());
        Review review2 = new Review();
        review2.setId(2);
        review2.setTab(new Tabulature());
        user.setReviews(Arrays.asList(review1, review2));

        //when(userRepository.save(user)).thenReturn(user);

        userService.delete(user);

        verify(tabulatureService, times(2)).getAverageRatingWithOutOne(anyInt(), anyInt());
        verify(userRepository).delete(user);
        assertEquals(0, review1.getTab().getRating());
        assertEquals(0, review2.getTab().getRating());
    }
}