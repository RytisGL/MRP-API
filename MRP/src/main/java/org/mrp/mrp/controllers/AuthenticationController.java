package org.mrp.mrp.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mrp.mrp.dto.auth.AuthenticationRequest;
import org.mrp.mrp.dto.auth.AuthenticationResponse;
import org.mrp.mrp.dto.auth.RegistrationRequest;
import org.mrp.mrp.dto.user.UserFetch;
import org.mrp.mrp.services.AuthenticationService;
import org.springframework.http.HttpStatus;
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
        return ResponseEntity.status(HttpStatus.CREATED).body(authenticationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@Valid @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
    //Record which user makes changes on records

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/authority/{userId}")
    public ResponseEntity<UserFetch> changeAuthority(@PathVariable Long userId, @RequestParam(value = "role") String role) {
        return ResponseEntity.ok(this.authenticationService.changeAuthority(userId, role));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<UserFetch> deleteUser(@PathVariable Long userId) {
        return ResponseEntity.ok(this.authenticationService.deleteUser(userId));
    }
}
