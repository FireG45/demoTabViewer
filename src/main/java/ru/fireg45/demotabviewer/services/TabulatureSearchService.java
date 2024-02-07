package ru.fireg45.demotabviewer.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.fireg45.demotabviewer.model.Tabulature;
import ru.fireg45.demotabviewer.repositories.TabulatureSearchRepository;

import java.util.List;

@Service
public class TabulatureSearchService {

    private final TabulatureSearchRepository searchRepository;

    @Autowired
    public TabulatureSearchService(TabulatureSearchRepository searchRepository) {
        this.searchRepository = searchRepository;
    }

    public List<Tabulature> search(String query) throws InterruptedException {
        return searchRepository.search(query);
    }
}
