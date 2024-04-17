package ru.fireg45.demotabviewer.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import ru.fireg45.demotabviewer.model.Tabulature;
import ru.fireg45.demotabviewer.model.User;
import ru.fireg45.demotabviewer.repositories.TabulaturesRepository;
import ru.fireg45.demotabviewer.tab.TabReader;
import ru.fireg45.demotabviewer.tab.dto.TabDTO;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TabulatureServiceTest {

    @Mock
    private TabulaturesRepository tabulaturesRepository;

    @Mock
    private FileService fileService;

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private TabulatureService tabulatureService;

    @BeforeEach
    void setUp() {
        // Setup mock responses or behavior here if needed
    }

    @Test
    void findById_ExistingId_ReturnsTabulature() {
        int id = 1;
        Tabulature expectedTabulature = new Tabulature(); // create expected tabulature object
        when(tabulaturesRepository.findById(id)).thenReturn(Optional.of(expectedTabulature));

        Optional<Tabulature> result = tabulatureService.findById(id);

        assertTrue(result.isPresent());
        assertEquals(expectedTabulature, result.get());
    }

    @Test
    void findById_NonExistingId_ReturnsEmptyOptional() {
        int id = 1;
        when(tabulaturesRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Tabulature> result = tabulatureService.findById(id);

        assertFalse(result.isPresent());
    }

    @Test
    void findAll_ReturnsListOfTabulatures() {
        // Setup mock response from repository
        List<Tabulature> expectedTabulatures = Arrays.asList(new Tabulature(), new Tabulature());
        when(tabulaturesRepository.findAll()).thenReturn(expectedTabulatures);

        List<Tabulature> result = tabulatureService.findAll();

        assertEquals(expectedTabulatures.size(), result.size());
        assertTrue(result.containsAll(expectedTabulatures));
    }

    @Test
    void findAllWithPageRequest_ReturnsListOfTabulatures() {
        // Setup mock response from repository
        List<Tabulature> expectedTabulatures = Arrays.asList(new Tabulature(), new Tabulature());
        PageRequest pageRequest = PageRequest.of(0, 10);
        org.springframework.data.domain.Page<Tabulature> page = new org.springframework.data.domain.PageImpl<>(expectedTabulatures, pageRequest, expectedTabulatures.size());
        when(tabulaturesRepository.findAll(pageRequest)).thenReturn(page);

        List<Tabulature> result = tabulatureService.findAll(pageRequest);

        assertEquals(expectedTabulatures.size(), result.size());
        assertTrue(result.containsAll(expectedTabulatures));
    }

    @Test
    void save_ValidTabulature_ReturnsSavedTabulature() {
        Tabulature tabulature = new Tabulature(); // create tabulature object
        when(tabulaturesRepository.save(tabulature)).thenReturn(tabulature);

        Tabulature result = tabulatureService.save(tabulature);

        assertEquals(tabulature, result);
    }

    @Test
    void delete_ValidTabulature_CallsRepositoryDelete() {
        Tabulature tabulature = new Tabulature(); // create tabulature object

        tabulatureService.delete(tabulature);

        verify(tabulaturesRepository).delete(tabulature);
    }

    @Test
    void getTabDto_ValidId_ReturnsTabDTO() throws IOException {
        int id = 1;
        Tabulature tabulature = new Tabulature(); // create tabulature object
        tabulature.setTitle("Test Title");
        tabulature.setAuthor("Test Author");
        tabulature.setUser(new User("testUser","test@example.com", "password", "USER"));
        tabulature.setUploaded(new Date());
        when(tabulaturesRepository.findById(id)).thenReturn(Optional.of(tabulature));

        InputStream mockInputStream = mock(InputStream.class);
        when(fileService.download(tabulature)).thenReturn(mockInputStream);

        TabReader mockTabReader = mock(TabReader.class);
        TabDTO expectedDTO = new TabDTO(); // create expected DTO object
        // Setup expectations for mockTabReader
        when(mockTabReader.read(eq(1), eq(null), eq(mockInputStream))).thenReturn(expectedDTO);

        Optional<User> user = Optional.of(new User(
                "testUser",
                "test@example.com",
                "password",
                "USER"
        ));
        String principal = "test@example.com";

        TabDTO result = tabulatureService.getTabDto(id, mockTabReader, 1, user, principal);

        assertNotNull(result);
        assertEquals("Test Title", result.title);
        assertEquals("Test Author", result.author);
        assertEquals(1, result.track);
        assertEquals("testUser", result.user);
        assertNotNull(result.uploaded);
        assertTrue(result.userOwner);
        assertFalse(result.favorite);
        assertEquals(0, result.rating);
    }

    @Test
    void findAllByAuthor_ReturnsListOfTabulatures() {
        String author = "Test Author";
        PageRequest pageRequest = PageRequest.of(0, 10);
        // Setup mock response from repository
        List<Tabulature> expectedTabulatures = Arrays.asList(new Tabulature(), new Tabulature());
        when(tabulaturesRepository.findAllByAuthor(author, pageRequest)).thenReturn(expectedTabulatures);

        List<Tabulature> result = tabulatureService.findAllByAuthor(author, pageRequest);

        assertEquals(expectedTabulatures.size(), result.size());
        assertTrue(result.containsAll(expectedTabulatures));
    }

    @Test
    void getAverageRating_ReturnsAverageRating() {
        int id = 1;
        int expectedAverageRating = 4;
        when(tabulaturesRepository.getAverageRating(id)).thenReturn(expectedAverageRating);

        int result = tabulatureService.getAverageRating(id);

        assertEquals(expectedAverageRating, result);
    }

    @Test
    void getAverageRatingWithOutOne_ReturnsAverageRating() {
        int id = 1;
        int reviewId = 2;
        Integer expectedAverageRating = 4;
        when(tabulaturesRepository.getAverageRating(id, reviewId)).thenReturn(expectedAverageRating);

        Integer result = tabulatureService.getAverageRatingWithOutOne(id, reviewId);

        assertEquals(expectedAverageRating, result);
    }

    @Test
    void getPageCount_ReturnsPageCount() {
        int pageSize = 10;
        long totalCount = 25;
        when(tabulaturesRepository.count()).thenReturn(totalCount);

        int expectedPageCount = 3;
        int result = tabulatureService.getPageCount(pageSize);

        assertEquals(expectedPageCount, result);
    }

    @Test
    void findFavoritesByEmail_ReturnsListOfTabulatures() {
        String email = "test@example.com";
        // Setup mock response from repository
        List<Tabulature> expectedTabulatures = Arrays.asList(new Tabulature(), new Tabulature());
        when(tabulaturesRepository.findFavoritesByEmail(email)).thenReturn(expectedTabulatures);

        List<Tabulature> result = tabulatureService.findFavoritesByEmail(email);

        assertEquals(expectedTabulatures.size(), result.size());
        assertTrue(result.containsAll(expectedTabulatures));
    }

    @Test
    void countFavorites_ReturnsCountOfFavorites() {
        Tabulature tabulature = new Tabulature();
        tabulature.setId(1);
        String email = "test@example.com";
        int expectedCount = 2;
        when(tabulaturesRepository.countFavorites(tabulature.getId(), email)).thenReturn(expectedCount);

        int result = tabulatureService.countFavorites(tabulature, email);

        assertEquals(expectedCount, result);
    }

}
