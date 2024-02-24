package ru.fireg45.demotabviewer.controllers;

import org.herac.tuxguitar.io.base.TGFileFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
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
import ru.fireg45.demotabviewer.tab.dto.TabDTO;
import ru.fireg45.demotabviewer.tab.TabReader;
import ru.fireg45.demotabviewer.tab.dto.TabListDTO;

import java.io.FileNotFoundException;
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
    private final FavoriteService favoriteService;
    private final TabulatureSearchService tabulatureSearchService;
    private final MidiService midiService;

    @Autowired
    public TabController(TabulatureService tabulatureService, UserService userService, TabReader tabReader,
                         FavoriteService favoriteService, TabulatureSearchService tabulatureSearchService,
                         MidiService midiService) {
        this.tabulatureService = tabulatureService;
        this.userService = userService;
        this.tabReader = tabReader;
        this.favoriteService = favoriteService;
        this.tabulatureSearchService = tabulatureSearchService;
        this.midiService = midiService;
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
        var principal = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = principal != null ? userService.findByEmail(principal) : Optional.empty();
        return tabulatureService.getTabDto(id, tabReader, track, user, principal);
    }

    @GetMapping("/tabs/midi/{id}")
    public ResponseEntity<Resource> getMidi(@PathVariable(name = "id") int id) throws FileNotFoundException {
        System.out.println("MIDI DOWNLOAD START");

        Optional<Tabulature> tabulature = tabulatureService.findById(id);

        if (tabulature.isEmpty()) return ResponseEntity.badRequest().build();

        Resource resource = midiService.convertToMidi(tabulature.get());

        System.out.println("MIDI DOWNLOAD OK");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"").body(resource);
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
