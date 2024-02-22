package ru.fireg45.demotabviewer.services;

import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.fireg45.demotabviewer.model.Tabulature;
import ru.fireg45.demotabviewer.model.User;
import ru.fireg45.demotabviewer.repositories.TabulaturesRepository;
import ru.fireg45.demotabviewer.tab.TabReader;
import ru.fireg45.demotabviewer.tab.dto.TabDTO;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TabulatureService {

    private final TabulaturesRepository tabulaturesRepository;
    private final FileService fileService;
    private final ReviewService reviewService;

    public TabulatureService(TabulaturesRepository tabulaturesRepository, FileService fileService, ReviewService reviewService) {
        this.tabulaturesRepository = tabulaturesRepository;
        this.fileService = fileService;
        this.reviewService = reviewService;
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

    public Integer getAverageRatingWithOutOne(int id, int reviewId) {
        return tabulaturesRepository.getAverageRating(id, reviewId);
    }

    public int getPageCount(int pageSize) {
        long totalCount = tabulaturesRepository.count();
        return totalCount < pageSize ? 1 : (int) (long) Math.ceil((double) totalCount / (double) pageSize);
    }

    public List<Tabulature> findFavoritesByEmail(String email) {
        return tabulaturesRepository.findFavoritesByEmail(email);
    }

    public int countFavorites(Tabulature tabulature, String email) {
        Integer count = tabulaturesRepository.countFavorites(tabulature.getId(), email);
        return count == null ? 0 : count;
    }

    public TabDTO getTabDto(int id, TabReader tabReader, int track, Optional<User> user, String principal) throws IOException {
        TabDTO tab;
        Optional<Tabulature> tabulatureOptional = findById(id);
        if (tabulatureOptional.isPresent()) {
            Tabulature tabulature = tabulatureOptional.get();
            InputStream stream = fileService.download(tabulature);
            tab = tabReader.read(track ,tabulature.getFilepath(), stream);
            tab.title = tabulature.getTitle();
            tab.author = tabulature.getAuthor();
            tab.track = track;
            tab.user = tabulature.getUser().getUsername();
            tab.uploaded = new SimpleDateFormat("yyyy-MM-dd").format(tabulature.getUploaded());
            tab.userOwner = principal != null && Objects.equals(tabulature.getUser().getEmail(), principal);
            tab.favorite = principal != null && countFavorites(tabulature, principal) != 0;
            if (user.isPresent()) {
                int userId = user.get().getId();
                Integer rating = reviewService.getRatingByUserAndTab(userId, id);
                tab.rating = rating == null ? 0 : rating;
            }
        } else {
            tab = null;
        }
        return tab;
    }

}
