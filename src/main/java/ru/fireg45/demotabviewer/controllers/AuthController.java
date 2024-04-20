package ru.fireg45.demotabviewer.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.fireg45.demotabviewer.model.User;
import ru.fireg45.demotabviewer.requests.LoginRequest;
import ru.fireg45.demotabviewer.requests.RegistrationRequest;
import ru.fireg45.demotabviewer.requests.UserUpdateRequest;
import ru.fireg45.demotabviewer.responses.LoginResponse;
import ru.fireg45.demotabviewer.responses.RegistrationResponse;
import ru.fireg45.demotabviewer.responses.UserResponse;
import ru.fireg45.demotabviewer.security.JWTIssuer;
import ru.fireg45.demotabviewer.security.UserPrincipal;
import ru.fireg45.demotabviewer.services.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin
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
    public LoginResponse login(@RequestBody @Valid LoginRequest request,
                               BindingResult result) {
        if (result.hasErrors()) {
            return new LoginResponse(getErrorList(result));
        }
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

    private static List<String> getErrorList(BindingResult result) {
        return result.getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
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
    public RegistrationResponse registration(@RequestBody @Valid RegistrationRequest request,
                                             BindingResult result) {
        if (result.hasErrors()) {
            return new RegistrationResponse(getErrorList(result));
        }

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

    @GetMapping("/auth/update")
    public ResponseEntity<UserResponse> getUpdate(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        User user = userService.findByEmail(principal.getEmail()).orElse(null);
        return new ResponseEntity<>(new UserResponse(user != null ? user.getUsername() : "",
                principal.getEmail()), HttpStatus.OK);
    }

    @GetMapping("/auth/role")
    public ResponseEntity<String> getRole(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        User user = userService.findByEmail(principal.getEmail()).orElse(null);
        if (user == null) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(user.getRole(), HttpStatus.OK);
    }

    @PostMapping("/auth/update")
    public ResponseEntity<UserResponse> update(@AuthenticationPrincipal UserPrincipal principal,
                                               @RequestBody @Valid UserUpdateRequest userUpdate,
                                               BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(new UserResponse(getErrorList(result)), HttpStatus.OK);
        }

        Optional<User> userOptional = userService.findByEmail(principal.getEmail());
        if (userOptional.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        User user = userOptional.get();
        user.setEmail(userUpdate.email);
        user.setUsername(userUpdate.username);
        userService.save(user);
        return new ResponseEntity<>(new UserResponse(user.getUsername(), user.getEmail()),
                HttpStatus.OK);
    }

    @DeleteMapping("/auth/delete")
    public ResponseEntity<String> delete(@AuthenticationPrincipal UserPrincipal principal) {
        Optional<User> userOptional = userService.findByEmail(principal.getEmail());
        if (userOptional.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        User user = userOptional.get();
        userService.delete(user);
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

}
