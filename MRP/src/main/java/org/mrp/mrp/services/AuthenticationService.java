package org.mrp.mrp.services;

import lombok.RequiredArgsConstructor;
import org.mrp.mrp.dto.auth.AuthenticationRequest;
import org.mrp.mrp.dto.auth.AuthenticationResponse;
import org.mrp.mrp.dto.auth.RegistrationRequest;
import org.mrp.mrp.entities.User;
import org.mrp.mrp.enums.Role;
import org.mrp.mrp.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegistrationRequest request) {
        User user = User.builder()
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .password(this.passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        userRepository.saveAndFlush(user);
        String token = this.jwtService.generateToken(user);
        return new AuthenticationResponse(token);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        User user = this.userRepository.findByEmail(request.getEmail()).orElseThrow();
        String token = this.jwtService.generateToken(user);
        return new AuthenticationResponse(token);
    }

    public void changeAuthority(Long userId, String role) {
        Role roleEnum = null;
        for (Role r : Role.values()) {
            if (r.toString().equals(role)) {
                roleEnum = r;
            }
        }
        if (roleEnum == null) {
            throw new NoSuchElementException();
        }
        User user = this.userRepository.findById(userId).orElseThrow();
        user.setRole(roleEnum);
        this.userRepository.saveAndFlush(user);
    }

    public void deleteUser(Long userId) {
        this.userRepository.deleteById(userId);
    }
}
