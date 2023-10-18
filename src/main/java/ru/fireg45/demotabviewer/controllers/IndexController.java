package ru.fireg45.demotabviewer.controllers;

import org.herac.tuxguitar.io.base.TGFileFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.fireg45.demotabviewer.model.Tabulature;
import ru.fireg45.demotabviewer.services.TabulatureService;
import ru.fireg45.demotabviewer.util.tabs.dto.TabDTO;
import ru.fireg45.demotabviewer.util.tabs.TabReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
public class IndexController {

    private final TabulatureService tabulatureService;
    private final TabReader tabReader;

    @Autowired
    public IndexController(TabulatureService tabulatureService, TabReader tabReader) {
        this.tabulatureService = tabulatureService;
        this.tabReader = tabReader;
    }

    @GetMapping("")
    public List<Tabulature> index(Model model) {
        return tabulatureService.findAll();
    }

    @GetMapping("/tabs/{id}")
    public TabDTO tabViewer(@PathVariable("id") int id, @RequestParam(name = "track", defaultValue = "0") int track,
                            Model model) throws TGFileFormatException, IOException {
        Tabulature tabulature = tabulatureService.findById(id).get();
        TabDTO tab = tabReader.read(track ,tabulature.getFilepath());
        tab.title = tabulature.getTitle();
        tab.author = tabulature.getAuthor();
        tab.track = track;
        return tab;
    }
}
