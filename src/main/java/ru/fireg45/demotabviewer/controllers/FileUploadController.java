package ru.fireg45.demotabviewer.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.resource.HttpResource;
import ru.fireg45.demotabviewer.model.Tabulature;
import ru.fireg45.demotabviewer.responses.FileUploadResponse;
import ru.fireg45.demotabviewer.services.FileService;
import ru.fireg45.demotabviewer.services.TabulatureService;

import java.net.http.HttpResponse;

@RestController
@CrossOrigin
public class FileUploadController {

    final String path = "/home/fireg/IdeaProjects/demoTabViewer/src/main/resources/static/test/";

    private final FileService fileService;
    private final TabulatureService tabulatureService;

    @Autowired
    public FileUploadController(FileService fileService, TabulatureService tabulatureService) {
        this.fileService = fileService;
        this.tabulatureService = tabulatureService;
    }

    @PostMapping(value="/upload")
    public FileUploadResponse handleFileUpload(@RequestParam("file") MultipartFile file,
                                                   @RequestParam("author") String author,
                                                   @RequestParam("title") String title) {
        Tabulature tab;
        try {
            String path = fileService.upload(file);
            tab = tabulatureService.save(new Tabulature(title, author, path));
        } catch (Exception ex) {
            return new FileUploadResponse(-1, HttpStatus.BAD_REQUEST);
        }
        return new FileUploadResponse(tab != null ? tab.getId() : -1, HttpStatus.OK);
    }

}
