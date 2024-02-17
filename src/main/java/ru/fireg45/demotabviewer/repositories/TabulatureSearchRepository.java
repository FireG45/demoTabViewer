package ru.fireg45.demotabviewer.repositories;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import ru.fireg45.demotabviewer.model.Tabulature;
import ru.fireg45.demotabviewer.util.Tuple;

import java.util.List;

@Component
@Repository
public interface TabulatureSearchRepository {
    Tuple<List<Tabulature>, Long> search(String query, int page, int pageSize, int pageCount) throws InterruptedException;
}
