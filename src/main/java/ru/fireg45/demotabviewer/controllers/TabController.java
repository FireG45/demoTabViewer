package ru.fireg45.demotabviewer.controllers;

import org.herac.tuxguitar.io.base.TGFileFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.fireg45.demotabviewer.model.Tabulature;
import ru.fireg45.demotabviewer.model.User;
import ru.fireg45.demotabviewer.security.UserPrincipal;
import ru.fireg45.demotabviewer.services.ReviewService;
import ru.fireg45.demotabviewer.services.TabulatureService;
import ru.fireg45.demotabviewer.services.UserService;
import ru.fireg45.demotabviewer.util.tabs.dto.TabDTO;
import ru.fireg45.demotabviewer.util.tabs.TabReader;
import ru.fireg45.demotabviewer.util.tabs.dto.TabListDTO;

import java.io.IOException;
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

    @Autowired
    public TabController(TabulatureService tabulatureService, UserService userService, TabReader tabReader, ReviewService reviewService) {
        this.tabulatureService = tabulatureService;
        this.userService = userService;
        this.tabReader = tabReader;
        this.reviewService = reviewService;
    }

    @GetMapping("")
    public TabListDTO index(@RequestParam(name = "page", defaultValue = "0") int page,
                            @RequestParam(name = "pageCount", defaultValue = "1") int pageCount,
                            @RequestParam(name = "author" , required = false) String author) {
        int PAGE_SIZE = 20;
        PageRequest pageRequest = PageRequest.of(page, PAGE_SIZE);
        List<Tabulature> tabs = author == null || author.isEmpty() ? tabulatureService.findAll(pageRequest) :
                tabulatureService.findAllByAuthor(author, pageRequest);
        return new TabListDTO(tabs, tabulatureService.getPageCount(PAGE_SIZE), page);
    }

    @GetMapping("/tabs/{id}")
    public TabDTO tabViewer(@PathVariable("id") int id,
                            @RequestParam(name = "track", defaultValue = "0") int track)
            throws TGFileFormatException, IOException {
        TabDTO tab;
        Optional<Tabulature> tabulatureOptional = tabulatureService.findById(id);
        if (tabulatureOptional.isPresent()) {
            Tabulature tabulature = tabulatureOptional.get();
            tab = tabReader.read(track ,tabulature.getFilepath());
            tab.title = tabulature.getTitle();
            tab.author = tabulature.getAuthor();
            tab.track = track;
            tab.user = tabulature.getUser().getUsername();
            tab.uploaded = new SimpleDateFormat("yyyy-MM-dd").format(tabulature.getUploaded());
            var principal = SecurityContextHolder.getContext().getAuthentication().getName();
            tab.userOwner = principal != null && Objects.equals(tabulature.getUser().getEmail(), principal);
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
}
