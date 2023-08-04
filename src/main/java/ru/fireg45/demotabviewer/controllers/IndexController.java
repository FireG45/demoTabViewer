package ru.fireg45.demotabviewer.controllers;

import org.herac.tuxguitar.io.base.TGFileFormatException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.fireg45.demotabviewer.util.TabReader;

import java.io.IOException;

@Controller
@RequestMapping()
public class IndexController {

    @GetMapping()
    public String index(@RequestParam("track") int track, Model model) throws TGFileFormatException, IOException {
        model.addAttribute("tabstaves", TabReader.read(track,
                "/home/fireg/IdeaProjects/demoTabViewer/src/main/resources/static/test/tab.gp5"));
        return "index";
    }
}
