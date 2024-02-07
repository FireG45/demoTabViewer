package ru.fireg45.demotabviewer.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.fireg45.demotabviewer.model.Tabulature;
import ru.fireg45.demotabviewer.repositories.TabulatureSearchRepository;
import ru.fireg45.demotabviewer.util.Tuple;

import java.util.List;

@Service
public class TabulatureSearchService {

    private final TabulatureSearchRepository searchRepository;

    @Autowired
    public TabulatureSearchService(@Qualifier("tabulatureSearchRepositoryImpl") TabulatureSearchRepository searchRepository) {
        this.searchRepository = searchRepository;
    }

    public Tuple<List<Tabulature>, Long> search(String query, int page, int pageSize, int pageCount) throws InterruptedException {
        return searchRepository.search(query, page, pageSize, pageCount);
    }
}
