package org.mrp.mrp.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mrp.mrp.dto.auth.AuthenticationRequest;
import org.mrp.mrp.dto.auth.AuthenticationResponse;
import org.mrp.mrp.dto.auth.RegistrationRequest;
import org.mrp.mrp.dto.user.UserFetch;
import org.mrp.mrp.entities.User;
import org.mrp.mrp.exceptions.customexceptions.UniqueDataException;
import org.mrp.mrp.repositories.UserRepository;
import org.mrp.mrp.utils.TestUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JWTService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    private User user;
    private RegistrationRequest registrationRequest;
    private AuthenticationRequest authenticationRequest;

    @BeforeEach
    void setUp() {
        user = TestUtils.getTestUser();
        authenticationRequest = TestUtils.getTestAuthenticationRequest();
        registrationRequest = TestUtils.getTestRegistrationRequest();


    }

    @Test
    void testRegisterSuccess() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(jwtService.generateToken(any(User.class))).thenReturn("jwtToken");
        when(userRepository.saveAndFlush(any(User.class))).thenReturn(user);

        AuthenticationResponse response = userService.register(registrationRequest);

        assertNotNull(response);
        assertEquals("jwtToken", response.getToken());
        verify(userRepository).saveAndFlush(any(User.class));
    }

    @Test
    void testRegisterEmailAlreadyExists() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        UniqueDataException exception = assertThrows(UniqueDataException.class,
                () -> userService.register(registrationRequest));
        assertEquals("exception.errors.email_unique.message", exception.getMessage());
        verify(userRepository, never()).saveAndFlush(any(User.class));
    }

    @Test
    void testAuthenticateSuccess() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(any(User.class))).thenReturn("jwtToken");

        AuthenticationResponse response = userService.authenticate(authenticationRequest);

        assertNotNull(response);
        assertEquals("jwtToken", response.getToken());
        verify(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void testAuthenticateUserNotFound() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> userService.authenticate(authenticationRequest));
        assertEquals("validation.constraints.email.name", exception.getMessage());
    }

    @Test
    void testChangeAuthoritySuccess() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.saveAndFlush(any(User.class))).thenReturn(user);

        UserFetch userFetch = userService.changeAuthority(1L, "ADMIN");

        assertNotNull(userFetch);
        verify(userRepository).saveAndFlush(any(User.class));
    }

    @Test
    void testChangeAuthorityRoleNotFound() {
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> userService.changeAuthority(1L,
                "INVALID_ROLE"));
        assertEquals("validation.constraints.role.name", exception.getMessage());
    }

    @Test
    void testDeleteUserSuccess() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        UserFetch userFetch = userService.deleteUser(1L);

        assertNotNull(userFetch);
        verify(userRepository).delete(any(User.class));
    }

    @Test
    void testDeleteUser_UserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> userService.deleteUser(1L));
        assertEquals("validation.constraints.user.name", exception.getMessage());
    }

    @Test
    void testGetUsersWithEmail() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        List<UserFetch> userFetches = userService.getUsers("john.doe@example.com");

        assertNotNull(userFetches);
        assertEquals(1, userFetches.size());
        verify(userRepository).findByEmail(anyString());
    }

    @Test
    void testGetUsers_WithoutEmail() {
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));

        List<UserFetch> userFetches = userService.getUsers(null);

        assertNotNull(userFetches);
        assertFalse(userFetches.isEmpty());
        verify(userRepository, times(1)).findAll();
    }
}
