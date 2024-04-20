package ru.fireg45.demotabviewer.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.fireg45.demotabviewer.model.Report;
import ru.fireg45.demotabviewer.model.Tabulature;
import ru.fireg45.demotabviewer.model.User;
import ru.fireg45.demotabviewer.repositories.ReportRepository;
import ru.fireg45.demotabviewer.security.UserPrincipal;
import ru.fireg45.demotabviewer.services.TabulatureService;
import ru.fireg45.demotabviewer.services.UserService;

import java.util.List;
import java.util.Optional;

@RestController
public class ReportController {

    private final UserService userService;
    private final ReportRepository reportRepository;
    private final TabulatureService tabulatureService;

    @Autowired
    public ReportController(UserService userService, ReportRepository reportRepository, TabulatureService tabulatureService) {
        this.userService = userService;
        this.reportRepository = reportRepository;
        this.tabulatureService = tabulatureService;
    }

    @GetMapping("/reports")
    public ResponseEntity<List<Report>> getAllReports(@AuthenticationPrincipal UserPrincipal principal) {
        Optional<User> userOptional = userService.findByEmail(principal.getEmail());
        if (userOptional.isEmpty()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        User user = userOptional.get();
        if (!user.getRole().equals("ADMIN")) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        return new ResponseEntity<>(reportRepository.findAll(), HttpStatus.OK);
    }

    @DeleteMapping("/reports/{id}")
    public ResponseEntity<String> getAllReports(@AuthenticationPrincipal UserPrincipal principal,
                                                      @PathVariable("id") int id) {
        Optional<User> userOptional = userService.findByEmail(principal.getEmail());
        if (userOptional.isEmpty()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        User user = userOptional.get();
        if (!user.getRole().equals("ADMIN")) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        Report report = reportRepository.findById(id).orElse(null);
        if (report == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        reportRepository.delete(report);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/reports/{id}")
    public ResponseEntity<String> addReport(@AuthenticationPrincipal UserPrincipal principal,
                                            @PathVariable("id") int id, @RequestBody String message) {
        Optional<User> userOptional = userService.findByEmail(principal.getEmail());
        if (userOptional.isEmpty()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        User user = userOptional.get();
        Optional<Tabulature> tabulatureOptional = tabulatureService.findById(id);
        if (tabulatureOptional.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        Tabulature tabulature = tabulatureOptional.get();

        Report report = new Report(tabulature, message, user);
        reportRepository.save(report);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
