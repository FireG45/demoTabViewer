package ru.fireg45.demotabviewer.controllers;

import org.herac.tuxguitar.io.base.TGFileFormatException;
import org.herac.tuxguitar.song.models.TGSong;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.fireg45.demotabviewer.util.TabReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping()
public class IndexController {

    @GetMapping()
    public String index(@RequestParam("track") int track, Model model) throws TGFileFormatException, IOException {
        List<String> tabs = new ArrayList<>();
        TGSong song = TabReader.read(track, tabs,
                "/home/fireg/IdeaProjects/demoTabViewer/src/main/resources/static/test/tab.gp5");
        model.addAttribute("tabs", tabs);
        model.addAttribute("name", song.getName());
        model.addAttribute("author", song.getArtist());
        model.addAttribute("track", song.getTrack(track).getName());
        return "index";
    }
}
