package org.mrp.mrp.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

class JWTServiceTest {

    @InjectMocks
    private JWTService jwtService;

    @Mock
    private UserDetails userDetails;

    private final String testUsername = "testUser";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(userDetails.getUsername()).thenReturn(testUsername);
    }

    @Test
    void testExtractUsername() {
        String token = generateTestToken(testUsername);
        String username = jwtService.extractUsername(token);
        assertEquals(testUsername, username);
    }

    @Test
    void testIsTokenValid() {
        String token = generateTestToken(testUsername);
        assertTrue(jwtService.isTokenValid(token, userDetails));
    }

    @Test
    void testExtractClaim() {
        String token = generateTestToken(testUsername);
        Claims claims = jwtService.extractClaim(token, Function.identity());
        assertEquals(testUsername, claims.getSubject());
    }

    @Test
    void testGenerateToken() {
        String token = jwtService.generateToken(userDetails);
        assertNotNull(token);
        assertEquals(testUsername, jwtService.extractUsername(token));
    }

    private String generateTestToken(String username) {
        return Jwts.builder()
                .setClaims(new HashMap<>())
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours expiration
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignInKey() {
        String secretKey = "a1b2c3d4e5f67890123456789abcdef0123456789abcdef0123456789abcdef";
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}