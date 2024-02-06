package ru.fireg45.demotabviewer.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.fireg45.demotabviewer.model.Tabulature;
import ru.fireg45.demotabviewer.model.User;
import ru.fireg45.demotabviewer.responses.FileUploadResponse;
import ru.fireg45.demotabviewer.responses.TabulatureUpdateResponse;
import ru.fireg45.demotabviewer.security.UserPrincipal;
import ru.fireg45.demotabviewer.services.FileService;
import ru.fireg45.demotabviewer.services.TabulatureService;
import ru.fireg45.demotabviewer.services.UserService;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

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
    public ResponseEntity<FileUploadResponse> handleFileUpload(@RequestParam("file") MultipartFile file,
                                               @RequestParam("author") String author,
                                               @RequestParam("title") String title,
                                               @AuthenticationPrincipal UserPrincipal principal) {
        Tabulature tab;
        User user = userService.findByEmail(principal.getEmail()).orElse(null);

        try {
            String path = fileService.upload(file);
            tab = tabulatureService.save(new Tabulature(title, author, path, user, 0));
        } catch (Exception ex) {
            return new ResponseEntity<>(new FileUploadResponse(-1), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new FileUploadResponse(tab != null ? tab.getId() : -1), HttpStatus.OK);
    }

    @GetMapping("/tabs/update/{id}")
    public ResponseEntity<TabulatureUpdateResponse> update(@PathVariable("id") int id) {
        Optional<Tabulature> tabOptional = tabulatureService.findById(id);
        if (tabOptional.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Tabulature tab = tabOptional.get();
        return new ResponseEntity<>(new TabulatureUpdateResponse(tab.getTitle(), tab.getAuthor()), HttpStatus.OK);
    }

    @PatchMapping("/tabs/update/{id}")
    public ResponseEntity<FileUploadResponse> update(@PathVariable("id") int id,
                                     @RequestParam(value = "file", required = false) MultipartFile file,
                                     @RequestParam("author") String author,
                                     @RequestParam("title") String title,
                                     @AuthenticationPrincipal UserPrincipal principal) {
        Optional<Tabulature> optionalTab = tabulatureService.findById(id);
        if (optionalTab.isEmpty()) return new ResponseEntity<>(new FileUploadResponse(-1), HttpStatus.BAD_REQUEST);

        Tabulature tab = optionalTab.get();
        if (principal == null || !Objects.equals(principal.getEmail(), tab.getUser().getEmail()))
            return new ResponseEntity<>(new FileUploadResponse(-1), HttpStatus.BAD_REQUEST);
        try {
            if (file != null) {
                fileService.delete(tab.getFilepath());
                String path = fileService.upload(file);
                tab.setFilepath(path);
            }
            tab.setAuthor(author);
            tab.setTitle(title);
            tab.setLastUpdate(new Date());
            tab = tabulatureService.save(tab);
        } catch (Exception ex) {
            return new ResponseEntity<>(new FileUploadResponse(-1), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new FileUploadResponse(tab != null ? tab.getId() : -1), HttpStatus.OK);
    }


    @DeleteMapping("/tabs/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") int id,
                                         @AuthenticationPrincipal UserPrincipal principal) {
        Optional<Tabulature> optionalTab = tabulatureService.findById(id);
        if (optionalTab.isEmpty()) return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);

        Tabulature tab = optionalTab.get();

        if (principal == null || !Objects.equals(principal.getEmail(), tab.getUser().getEmail()))
            return new ResponseEntity<>("", HttpStatus.FORBIDDEN);

        try {
            fileService.delete(tab.getFilepath());
            tabulatureService.delete(tab);
        } catch (Exception ex) {
            return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("", HttpStatus.OK);
    }
}
