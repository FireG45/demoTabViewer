package ru.fireg45.demotabviewer.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.fireg45.demotabviewer.model.Tabulature;
import ru.fireg45.demotabviewer.services.FileService;
import ru.fireg45.demotabviewer.services.TabulatureService;
import ru.fireg45.demotabviewer.util.FileUploadException;

@Controller
public class FileUploadController {

    final String path = "/home/fireg/IdeaProjects/demoTabViewer/src/main/resources/static/test/";

    private final FileService fileService;
    private final TabulatureService tabulatureService;

    @Autowired
    public FileUploadController(FileService fileService, TabulatureService tabulatureService) {
        this.fileService = fileService;
        this.tabulatureService = tabulatureService;
    }

    @GetMapping("/upload")
    public String upload() {
        return "upload";
    }

    @RequestMapping(value="/upload", method= RequestMethod.POST)
    public String handleFileUpload(@RequestParam("title") String title,
                                                 @RequestParam("author") String author,
                                                 @RequestParam("file") MultipartFile file) {
        try {
            String path = fileService.upload(file);
            tabulatureService.save(new Tabulature(title, author, path));
        } catch (Exception ex) {
            return "error";
        }
        return "index";
    }

}
