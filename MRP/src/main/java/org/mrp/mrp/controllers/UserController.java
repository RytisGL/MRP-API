package org.mrp.mrp.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mrp.mrp.dto.auth.AuthenticationRequest;
import org.mrp.mrp.dto.auth.AuthenticationResponse;
import org.mrp.mrp.dto.auth.RegistrationRequest;
import org.mrp.mrp.dto.user.UserFetch;
import org.mrp.mrp.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserFetch>> getUsers() {
        return ResponseEntity.ok(this.userService.getUsers());
    }

    @GetMapping("/{email}")
    public ResponseEntity<UserFetch> getUser(@PathVariable String email) {
        return ResponseEntity.ok(this.userService.getUserByEmail(email));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/{userId}/authority")
    public ResponseEntity<UserFetch> changeAuthority(@PathVariable Long userId, @RequestParam(value = "role") String role) {
        return ResponseEntity.ok(this.userService.changeAuthority(userId, role));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody RegistrationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@Valid @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(userService.authenticate(request));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<UserFetch> deleteUser(@PathVariable Long userId) {
        return ResponseEntity.ok(this.userService.deleteUser(userId));
    }
}
