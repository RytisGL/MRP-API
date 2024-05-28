package org.mrp.mrp.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mrp.mrp.dto.auth.AuthenticationRequest;
import org.mrp.mrp.dto.auth.AuthenticationResponse;
import org.mrp.mrp.dto.auth.RegistrationRequest;
import org.mrp.mrp.services.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody RegistrationRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@Valid @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    //User DTO to return instead of Void.

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/authority/{userId}")
    public ResponseEntity<Void> changeAuthority(@PathVariable Long userId, @RequestParam(value = "role") String role) {
        this.authenticationService.changeAuthority(userId, role);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        this.authenticationService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }
}
