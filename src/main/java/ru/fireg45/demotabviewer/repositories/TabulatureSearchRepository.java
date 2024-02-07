package ru.fireg45.demotabviewer.repositories;

import org.springframework.stereotype.Repository;
import ru.fireg45.demotabviewer.model.Tabulature;

import java.util.List;

@Repository
public interface TabulatureSearchRepository {
    List<Tabulature> search(String query) throws InterruptedException;
}
