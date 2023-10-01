package ru.fireg45.demotabviewer.controllers;

import org.herac.tuxguitar.io.base.TGFileFormatException;
import org.herac.tuxguitar.song.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.fireg45.demotabviewer.model.Tabulature;
import ru.fireg45.demotabviewer.services.TabulatureService;
import ru.fireg45.demotabviewer.util.TabReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
public class IndexController {

    private final TabulatureService tabulatureService;

    @Autowired
    public IndexController(TabulatureService tabulatureService) {
        this.tabulatureService = tabulatureService;
    }

    @GetMapping("")
    public List<Tabulature> index(Model model) {
        return tabulatureService.findAll();
    }

    @GetMapping("/tabs/{id}")
    public Tabulature tabViewer(@PathVariable("id") int id, @RequestParam(name = "track", defaultValue = "0") int track,
                            Model model) throws TGFileFormatException, IOException {
        Optional<Tabulature> tabulature = tabulatureService.findById(id);
//        if (tabulature.isEmpty()) {
//            return "redirect:error";
//        }
        List<String> tabs = new ArrayList<>();
        TGSong song = TabReader.read(track, tabs,tabulature.get().getFilepath());
        model.addAttribute("tabs", tabs);
        model.addAttribute("tabId", id);
        model.addAttribute("name", song.getName().isBlank() ? tabulature.get().getTitle() : song.getName());
        model.addAttribute("author", song.getArtist().isBlank() ? tabulature.get().getAuthor() : song.getArtist());
        model.addAttribute("track", song.getTrack(track).getName());
        model.addAttribute("trackId", track);
        model.addAttribute("trackList", TabReader.getTrackNames(song.getTracks()));
        model.addAttribute("tuning", song.getTrack(track).getTuning());
        model.addAttribute("tempo", song.getMeasureHeader(0).getTempo());
        return tabulature.get();
    }
}
