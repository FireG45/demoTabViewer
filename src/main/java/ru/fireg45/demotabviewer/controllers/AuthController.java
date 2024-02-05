package ru.fireg45.demotabviewer.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.fireg45.demotabviewer.model.User;
import ru.fireg45.demotabviewer.requests.LoginRequest;
import ru.fireg45.demotabviewer.requests.RegistrationRequest;
import ru.fireg45.demotabviewer.responses.LoginResponse;
import ru.fireg45.demotabviewer.responses.RegistrationResponse;
import ru.fireg45.demotabviewer.responses.UserResponse;
import ru.fireg45.demotabviewer.security.JWTIssuer;
import ru.fireg45.demotabviewer.security.UserPrincipal;
import ru.fireg45.demotabviewer.services.UserService;

import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class AuthController {

    private final JWTIssuer jwtIssuer;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(JWTIssuer jwtIssuer, AuthenticationManager authenticationManager, UserService userService, PasswordEncoder passwordEncoder) {
        this.jwtIssuer = jwtIssuer;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/auth/login")
    public LoginResponse login(@RequestBody @Validated LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        var principal = (UserPrincipal) authentication.getPrincipal();

        var roles = principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        var token = jwtIssuer.issue(principal.getUserId(), principal.getEmail(), roles);

        User user = userService.findByEmail(request.getEmail()).orElse(null);

        return new LoginResponse(user != null ? user.getUsername() : "", token, HttpStatus.OK);
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<String> logout(@AuthenticationPrincipal UserPrincipal principal) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getName().equals(principal.getUsername())) {
            auth.setAuthenticated(false);
            System.out.println(auth.getName() + "logged out");
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/auth/register")
    public RegistrationResponse registration(@RequestBody RegistrationRequest request) {
        if (userService.findByUsername(request.getUsername()).isPresent() ||
                userService.findByEmail(request.getEmail()).isPresent())
            return new RegistrationResponse(HttpStatus.BAD_REQUEST);

        User user = new User(request.getUsername(), request.getEmail(),
                passwordEncoder.encode(request.getPassword()), "USER");

        userService.save(user);
        return new RegistrationResponse(HttpStatus.OK);
    }

    @GetMapping("/auth/getuser")
    public UserResponse getUser(@AuthenticationPrincipal UserPrincipal principal) {
        return new UserResponse(principal.getUsername());
    }

}
