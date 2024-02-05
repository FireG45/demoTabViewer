package ru.fireg45.demotabviewer.controllers;

import org.herac.tuxguitar.io.base.TGFileFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.fireg45.demotabviewer.model.Tabulature;
import ru.fireg45.demotabviewer.model.User;
import ru.fireg45.demotabviewer.responses.FileUploadResponse;
import ru.fireg45.demotabviewer.security.UserPrincipal;
import ru.fireg45.demotabviewer.services.TabulatureService;
import ru.fireg45.demotabviewer.services.UserService;
import ru.fireg45.demotabviewer.util.tabs.dto.TabDTO;
import ru.fireg45.demotabviewer.util.tabs.TabReader;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@CrossOrigin
public class IndexController {

    private final TabulatureService tabulatureService;
    private final UserService userService;
    private final TabReader tabReader;

    @Autowired
    public IndexController(TabulatureService tabulatureService, UserService userService, TabReader tabReader) {
        this.tabulatureService = tabulatureService;
        this.userService = userService;
        this.tabReader = tabReader;
    }

    @GetMapping("")
    public List<Tabulature> index() {
        return tabulatureService.findAll();
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
