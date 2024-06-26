package org.mrp.mrp.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.mrp.mrp.converters.UserConverter;
import org.mrp.mrp.dto.auth.AuthenticationRequest;
import org.mrp.mrp.dto.auth.AuthenticationResponse;
import org.mrp.mrp.dto.auth.RegistrationRequest;
import org.mrp.mrp.dto.user.UserFetch;
import org.mrp.mrp.entities.User;
import org.mrp.mrp.enums.Role;
import org.mrp.mrp.exceptions.customexceptions.UniqueDataException;
import org.mrp.mrp.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    private static final String USER_EXCEPTION_MESSAGE = "validation.constraints.user.name";

    public AuthenticationResponse register(RegistrationRequest request) {
        if (this.userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UniqueDataException("exception.errors.email_unique.message");
        }
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
        this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        User user = this.userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("validation.constraints.email.name"));

        String token = this.jwtService.generateToken(user);
        return new AuthenticationResponse(token);
    }

    public UserFetch changeAuthority(Long userId, String role) {
        Role roleEnum = null;
        for (Role r : Role.values()) {
            if (r.toString().equalsIgnoreCase(role)) {
                roleEnum = r;
            }
        }
        if (roleEnum == null) {
            throw new EntityNotFoundException("validation.constraints.role.name");
        }

        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(USER_EXCEPTION_MESSAGE));

        user.setRole(roleEnum);
        this.userRepository.saveAndFlush(user);
        return UserConverter.userToUserDTO(user);
    }

    public UserFetch deleteUser(Long userId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(USER_EXCEPTION_MESSAGE));

        this.userRepository.delete(user);
        return UserConverter.userToUserDTO(user);
    }

    public List<UserFetch> getUsers(String email) {
        if (email != null) {
            List<UserFetch> userFetches = new ArrayList<>();

            userFetches.add(UserConverter.userToUserDTO(this.userRepository.findByEmail(email)
                    .orElseThrow(() -> new EntityNotFoundException("validation.constraints.email.name"))));

            return userFetches;
        }
        return UserConverter.usersToUserDTOs(this.userRepository.findAll());
    }
}
