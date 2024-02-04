package ru.fireg45.demotabviewer.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.fireg45.demotabviewer.model.Tabulature;
import ru.fireg45.demotabviewer.model.User;
import ru.fireg45.demotabviewer.responses.FileUploadResponse;
import ru.fireg45.demotabviewer.security.UserPrincipal;
import ru.fireg45.demotabviewer.services.FileService;
import ru.fireg45.demotabviewer.services.TabulatureService;
import ru.fireg45.demotabviewer.services.UserService;

@RestController
@CrossOrigin
public class FileUploadController {

    @Value("${upload.defaultFilePath}")
    private String path;

    private final FileService fileService;
    private final UserService userService;
    private final TabulatureService tabulatureService;

    @Autowired
    public FileUploadController(FileService fileService, UserService userService, TabulatureService tabulatureService) {
        this.fileService = fileService;
        this.userService = userService;
        this.tabulatureService = tabulatureService;
    }

    @PostMapping(value = "/upload")
    public FileUploadResponse handleFileUpload(@RequestParam("file") MultipartFile file,
                                               @RequestParam("author") String author,
                                               @RequestParam("title") String title,
                                               @AuthenticationPrincipal UserPrincipal principal) {
        Tabulature tab;
        User user = userService.findByEmail(principal.getEmail()).orElse(null);

        try {
            String path = fileService.upload(file);
            tab = tabulatureService.save(new Tabulature(title, author, path, user));
        } catch (Exception ex) {
            return new FileUploadResponse(-1, HttpStatus.BAD_REQUEST);
        }
        return new FileUploadResponse(tab != null ? tab.getId() : -1, HttpStatus.OK);
    }
}
