package ru.fireg45.demotabviewer.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.fireg45.demotabviewer.model.User;
import ru.fireg45.demotabviewer.requests.LoginRequest;
import ru.fireg45.demotabviewer.requests.RegistrationRequest;
import ru.fireg45.demotabviewer.responses.LoginResponse;
import ru.fireg45.demotabviewer.security.JWTIssuer;
import ru.fireg45.demotabviewer.security.UserPrincipal;
import ru.fireg45.demotabviewer.services.UserService;

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

        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        var principal = (UserPrincipal) authentication.getPrincipal();

        var roles = principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        var token = jwtIssuer.issue(principal.getUserId(), principal.getEmail(), roles);
        return LoginResponse.builder()
                .accessToken(token)
                .build();
    }

    @PostMapping("/auth/register")
    public ResponseEntity<String> registration(@RequestBody @Validated RegistrationRequest request) {
        if (userService.findByUsername(request.getUsername()).isPresent() ||
                userService.findByEmail(request.getEmail()).isPresent())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        User user = new User(request.getUsername(), request.getEmail(),
                passwordEncoder.encode(request.getPassword()), "USER");

        userService.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
