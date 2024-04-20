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
import ru.fireg45.demotabviewer.tab.TabEditor;
import ru.fireg45.demotabviewer.tab.dto.NoteChangesDTO;
import ru.fireg45.demotabviewer.tab.dto.TabChangesDTO;
import ru.fireg45.demotabviewer.tab.dto.TabDTO;
import ru.fireg45.demotabviewer.tab.TabReader;
import ru.fireg45.demotabviewer.tab.dto.TabListDTO;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
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
    private final TabEditor tabEditor;

    @Autowired
    public TabController(TabulatureService tabulatureService, UserService userService, TabReader tabReader,
                         FavoriteService favoriteService, TabulatureSearchService tabulatureSearchService,
                         MidiService midiService, TabEditor tabEditor) {
        this.tabulatureService = tabulatureService;
        this.userService = userService;
        this.tabReader = tabReader;
        this.favoriteService = favoriteService;
        this.tabulatureSearchService = tabulatureSearchService;
        this.midiService = midiService;
        this.tabEditor = tabEditor;
    }

    @GetMapping("")
    public TabListDTO index(@RequestParam(name = "page", defaultValue = "0") int page,
                            @RequestParam(name = "pageCount", defaultValue = "1") int pageCount,
                            @RequestParam(name = "author", required = false) String author,
                            @RequestParam(name = "query", required = false) String query) throws InterruptedException {
        int PAGE_SIZE = 20;
        List<Tabulature> tabs;
        long pagesCount;
        System.out.println("QUERY: " + query);
        if (query == null || query.isEmpty()) {
            System.out.println("QUERY: NO");
            PageRequest pageRequest = PageRequest.of(page, PAGE_SIZE);
            tabs = author == null || author.isEmpty() ? tabulatureService.findAll(pageRequest) :
                    tabulatureService.findAllByAuthor(author, pageRequest);
            pagesCount = tabulatureService.getPageCount(PAGE_SIZE);
        } else {
            System.out.println("QUERY: YES");
            var tuple = tabulatureSearchService.search(query);
            pagesCount = 0;
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
        Optional<Tabulature> tabulature = tabulatureService.findById(id);

        if (tabulature.isEmpty()) return ResponseEntity.badRequest().build();

        Resource resource = midiService.convertToMidi(tabulature.get());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"").body(resource);
    }

    @GetMapping("/tabs/mytabs")
    public ResponseEntity<List<Tabulature>> myTabs(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        Optional<User> user = userService.findByEmail(principal.getEmail());
        return new ResponseEntity<>(user.map(User::getTabulatures).orElse(null), HttpStatus.OK);
    }

    @GetMapping("tabs/favorite")
    public ResponseEntity<List<Tabulature>> favoriteTabs(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(tabulatureService.findFavoritesByEmail(principal.getEmail()), HttpStatus.OK);
    }

    @PostMapping("tabs/setfavorite/{id}")
    public ResponseEntity<String> setFavorite(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                              @PathVariable("id") int id) {
        if (userPrincipal == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        Optional<User> optionalUser = userService.findByEmail(userPrincipal.getEmail());
        Optional<Tabulature> tabulature = tabulatureService.findById(id);
        if (optionalUser.isEmpty() || tabulature.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        favoriteService.save(new Favorite(optionalUser.get(), tabulature.get()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("tabs/removefavorite/{id}")
    public ResponseEntity<String> removeFavorite(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                 @PathVariable("id") int id) {
        if (userPrincipal == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        Optional<Favorite> favorite = favoriteService.getFavorite(userPrincipal.getEmail(), id);
        if (favorite.isEmpty()) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        favoriteService.delete(favorite.get());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("edit/{id}")
    public ResponseEntity<String> checkEditPermit(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                  @PathVariable("id") int id) {
        Optional<User> optionalUser = userService.findByEmail(userPrincipal.getEmail());
        if (optionalUser.isEmpty() || !optionalUser.get().getRole().equals("ADMIN") &&
                optionalUser.get().getTabulatures().stream()
                        .filter(tabulature -> tabulature.getId() == id).findAny().isEmpty())
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("edit/{id}/{track}")
    public ResponseEntity<String> editTab(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                          @PathVariable("id") int id, @PathVariable("track") int track,
                                          @RequestBody TabChangesDTO tabChanges) throws IOException {
        Optional<User> optionalUser = userService.findByEmail(userPrincipal.getEmail());
        if (optionalUser.isEmpty() || !optionalUser.get().getRole().equals("ADMIN") &&
                optionalUser.get().getTabulatures().stream()
                        .filter(tabulature -> tabulature.getId() == id).findAny().isEmpty())
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        tabEditor.updateSong(id, track, tabChanges);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/teapot")
    public ResponseEntity<String> teapot() {
        return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
    }

    @ExceptionHandler(com.auth0.jwt.exceptions.TokenExpiredException.class)
    public ResponseEntity<String> handleTokenExpiredException(com.auth0.jwt.exceptions.TokenExpiredException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }
}
