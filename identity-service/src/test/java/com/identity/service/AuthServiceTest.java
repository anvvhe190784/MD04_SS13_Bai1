package com.identity.service;

import com.identity.dto.RegisterRequest;
import com.identity.dto.UserResponse;
import com.identity.entity.User;
import com.identity.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(userRepository, passwordEncoder);
    }

    @Test
    void register_Success() {
        RegisterRequest request = new RegisterRequest("alice", "rawPassword123", "USER");
        when(userRepository.existsByUsername("alice")).thenReturn(false);
        when(passwordEncoder.encode("rawPassword123")).thenReturn("$2a$10$hashedPasswordHere");

        UUID generatedId = UUID.randomUUID();
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            u.setId(generatedId);
            return u;
        });

        UserResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals("alice", response.username());
        assertEquals("USER", response.role());
        assertEquals(generatedId, response.id());
        verify(passwordEncoder).encode("rawPassword123");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_DuplicateUsername_ThrowsException() {
        RegisterRequest request = new RegisterRequest("alice", "pass", "USER");
        when(userRepository.existsByUsername("alice")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> authService.register(request));
        verify(userRepository, never()).save(any());
    }
}
