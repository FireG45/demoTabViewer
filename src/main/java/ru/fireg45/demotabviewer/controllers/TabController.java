package ru.fireg45.demotabviewer.controllers;

import org.herac.tuxguitar.io.base.TGFileFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.fireg45.demotabviewer.model.Favorite;
import ru.fireg45.demotabviewer.model.Tabulature;
import ru.fireg45.demotabviewer.model.User;
import ru.fireg45.demotabviewer.security.UserPrincipal;
import ru.fireg45.demotabviewer.services.*;
import ru.fireg45.demotabviewer.util.tabs.dto.TabDTO;
import ru.fireg45.demotabviewer.util.tabs.TabReader;
import ru.fireg45.demotabviewer.util.tabs.dto.TabListDTO;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@CrossOrigin
public class TabController {

    private final TabulatureService tabulatureService;
    private final UserService userService;
    private final TabReader tabReader;
    private final ReviewService reviewService;
    private final FavoriteService favoriteService;
    private final TabulatureSearchService tabulatureSearchService;
    private final FileService fileService;

    @Autowired
    public TabController(TabulatureService tabulatureService, UserService userService, TabReader tabReader,
                         ReviewService reviewService, FavoriteService favoriteService,
                         TabulatureSearchService tabulatureSearchService, FileService fileService) {
        this.tabulatureService = tabulatureService;
        this.userService = userService;
        this.tabReader = tabReader;
        this.reviewService = reviewService;
        this.favoriteService = favoriteService;
        this.tabulatureSearchService = tabulatureSearchService;
        this.fileService = fileService;
    }

    @GetMapping("")
    public TabListDTO index(@RequestParam(name = "page", defaultValue = "0") int page,
                            @RequestParam(name = "pageCount", defaultValue = "1") int pageCount,
                            @RequestParam(name = "author" , required = false) String author,
                            @RequestParam(name = "query" , required = false) String query) throws InterruptedException {
        int PAGE_SIZE = 20;
        List<Tabulature> tabs;
        long pagesCount;
        if (query == null) {
            PageRequest pageRequest = PageRequest.of(page, PAGE_SIZE);
            tabs = author == null || author.isEmpty() ? tabulatureService.findAll(pageRequest) :
                    tabulatureService.findAllByAuthor(author, pageRequest);
            pagesCount = tabulatureService.getPageCount(PAGE_SIZE);
        } else {
            var tuple = tabulatureSearchService.search(query, page, PAGE_SIZE, pageCount);
            pagesCount = tuple.getValue2();
            tabs = tuple.getValue1();
        }

        return new TabListDTO(tabs, (int) pagesCount, page);
    }

    @GetMapping("/tabs/{id}")
    public TabDTO tabViewer(@PathVariable("id") int id,
                            @RequestParam(name = "track", defaultValue = "0") int track)
            throws TGFileFormatException, IOException {
        TabDTO tab;
        Optional<Tabulature> tabulatureOptional = tabulatureService.findById(id);
        if (tabulatureOptional.isPresent()) {
            Tabulature tabulature = tabulatureOptional.get();
            InputStream stream = fileService.download(tabulature);
            tab = tabReader.read(track ,tabulature.getFilepath(), stream);
            tab.title = tabulature.getTitle();
            tab.author = tabulature.getAuthor();
            tab.track = track;
            tab.user = tabulature.getUser().getUsername();
            tab.uploaded = new SimpleDateFormat("yyyy-MM-dd").format(tabulature.getUploaded());
            var principal = SecurityContextHolder.getContext().getAuthentication().getName();
            tab.userOwner = principal != null && Objects.equals(tabulature.getUser().getEmail(), principal);
            tab.favorite = tabulatureService.countFavorites(tabulature, principal) != 0;
            Optional<User> user = userService.findByEmail(principal);
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

    @GetMapping("/tabs/mytabs")
    public List<Tabulature> myTabs(@AuthenticationPrincipal UserPrincipal principal) {
        Optional<User> user = userService.findByEmail(principal.getEmail());
        return user.map(User::getTabulatures).orElse(null);
    }

    @GetMapping("tabs/favorite")
    public List<Tabulature> favoriteTabs(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) return null;
        return tabulatureService.findFavoritesByEmail(principal.getEmail());
    }

    @PostMapping("tabs/setfavorite/{id}")
    public ResponseEntity<String> setFavorite(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                              @PathVariable("id") int id) {
        if (userPrincipal == null) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        Optional<User> optionalUser = userService.findByEmail(userPrincipal.getEmail());
        Optional<Tabulature> tabulature = tabulatureService.findById(id);
        if (optionalUser.isEmpty() || tabulature.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        favoriteService.save(new Favorite(optionalUser.get(), tabulature.get()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("tabs/removefavorite/{id}")
    public ResponseEntity<String> removeFavorite(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                              @PathVariable("id") int id) {
        if (userPrincipal == null) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        Optional<Favorite> favorite = favoriteService.getFavorite(userPrincipal.getEmail(), id);
        if (favorite.isEmpty()) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        favoriteService.delete(favorite.get());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
