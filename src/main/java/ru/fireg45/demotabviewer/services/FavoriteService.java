package ru.fireg45.demotabviewer.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.fireg45.demotabviewer.model.Favorite;
import ru.fireg45.demotabviewer.model.Tabulature;
import ru.fireg45.demotabviewer.repositories.FavoriteRepository;

import java.util.List;
import java.util.Optional;

@Service
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;

    @Autowired
    public FavoriteService(FavoriteRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }

    public Optional<Favorite> getFavorite(String email, int tabId) {
        return favoriteRepository.getFavorite(email, tabId);
    }

    public void save(Favorite favorite) {
        favoriteRepository.save(favorite);
    }

    public void delete(Favorite favorite) {
        favoriteRepository.delete(favorite);
    }
}
