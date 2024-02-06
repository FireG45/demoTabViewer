package ru.fireg45.demotabviewer.services;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.fireg45.demotabviewer.model.Tabulature;
import ru.fireg45.demotabviewer.repositories.TabulaturesRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TabulatureService {

    private final TabulaturesRepository tabulaturesRepository;

    public TabulatureService(TabulaturesRepository tabulaturesRepository) {
        this.tabulaturesRepository = tabulaturesRepository;
    }

    public Optional<Tabulature> findById(int id) {
        return tabulaturesRepository.findById(id);
    }

    public List<Tabulature> findAll() {
        return tabulaturesRepository.findAll();
    }

    public List<Tabulature> findAll(PageRequest pageRequest) {
        return tabulaturesRepository.findAll(pageRequest).getContent();
    }

    public Tabulature save(Tabulature tabulature) {
        return tabulaturesRepository.save(tabulature);
    }

    public void delete(Tabulature tabulature) { tabulaturesRepository.delete(tabulature); }

    public List<Tabulature> findAllByAuthor(String author, PageRequest pageRequest) {
        return tabulaturesRepository.findAllByAuthor(author, pageRequest);
    }

    public int getAverageRating(int id) {
        return tabulaturesRepository.getAverageRating(id);
    }

    public int getPageCount(int pageSize) {
        long totalCount = tabulaturesRepository.count();
        return totalCount < pageSize ? 1 : (int) (long) Math.ceil((double) totalCount / (double) pageSize);
    }
}
