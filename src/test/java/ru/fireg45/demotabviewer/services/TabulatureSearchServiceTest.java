package ru.fireg45.demotabviewer.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.fireg45.demotabviewer.model.Tabulature;
import ru.fireg45.demotabviewer.repositories.TabulatureSearchRepository;
import ru.fireg45.demotabviewer.util.Tuple;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TabulatureSearchServiceTest {

    @Mock
    private TabulatureSearchRepository searchRepository;

    @InjectMocks
    private TabulatureSearchService searchService;

    @Test
    void search_ReturnsListOfTabulaturesAndTotalCount() throws InterruptedException {
        String query = "test";
        List<Tabulature> expectedTabulatures = new ArrayList<>();
        expectedTabulatures.add(new Tabulature());
        long expectedTotalCount = 10;
        when(searchRepository.search(query)).thenReturn(new Tuple<>(expectedTabulatures, expectedTotalCount));

        Tuple<List<Tabulature>, Long> result = searchService.search(query);

        assertEquals(expectedTabulatures, result.getValue1());
        assertEquals(expectedTotalCount, result.getValue2());
    }

    @Test
    void search_EmptyQuery_ReturnsEmptyListAndZeroTotalCount() throws InterruptedException {
        String query = "";
        List<Tabulature> expectedTabulatures = new ArrayList<>();
        long expectedTotalCount = 0;
        when(searchRepository.search(query)).thenReturn(new Tuple<>(expectedTabulatures, expectedTotalCount));

        Tuple<List<Tabulature>, Long> result = searchService.search(query);

        assertTrue(result.getValue1().isEmpty());
        assertEquals(expectedTotalCount, result.getValue2());
    }
}
