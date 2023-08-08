package ru.fireg45.demotabviewer.controllers;

import org.herac.tuxguitar.graphics.control.TGFactoryImpl;
import org.herac.tuxguitar.io.base.TGFileFormatException;
import org.herac.tuxguitar.song.helpers.tuning.TuningGroup;
import org.herac.tuxguitar.song.helpers.tuning.TuningManager;
import org.herac.tuxguitar.song.helpers.tuning.xml.TuningReader;
import org.herac.tuxguitar.song.managers.TGSongManager;
import org.herac.tuxguitar.song.managers.TGTrackManager;
import org.herac.tuxguitar.song.models.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.fireg45.demotabviewer.util.TabReader;

import java.io.FileInputStream;
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
                "/home/fireg/IdeaProjects/demoTabViewer/src/main/resources/static/test/tab3.gp5");
        model.addAttribute("tabs", tabs);
        model.addAttribute("name", song.getName());
        model.addAttribute("author", song.getArtist());
        model.addAttribute("track", song.getTrack(track).getName());
        model.addAttribute("trackId", track);
        model.addAttribute("trackList", TabReader.getTrackNames(song.getTracks()));
        model.addAttribute("tuning", song.getTrack(track).getTuning());
        model.addAttribute("tempo", song.getMeasureHeader(0).getTempo());
        return "index";
    }
}
