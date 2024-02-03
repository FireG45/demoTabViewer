package ru.fireg45.demotabviewer.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.fireg45.demotabviewer.model.Tabulature;
import ru.fireg45.demotabviewer.responses.FileUploadResponse;
import ru.fireg45.demotabviewer.services.FileService;
import ru.fireg45.demotabviewer.services.TabulatureService;

@RestController
@CrossOrigin
public class FileUploadController {

    @Value("${upload.defaultFilePath}")
    private String path;

    private final FileService fileService;
    private final TabulatureService tabulatureService;

    @Autowired
    public FileUploadController(FileService fileService, TabulatureService tabulatureService) {
        this.fileService = fileService;
        this.tabulatureService = tabulatureService;
    }

    @PostMapping(value = "/upload")
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
