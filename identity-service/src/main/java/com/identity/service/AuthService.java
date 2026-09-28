package com.identity.service;

import com.identity.dto.RegisterRequest;
import com.identity.dto.UserResponse;
import com.identity.entity.User;
import com.identity.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("Username already exists");
        }

        String encodedPassword = passwordEncoder.encode(request.password());
        String role = (request.role() == null || request.role().isBlank()) ? "USER" : request.role().toUpperCase();

        User user = new User(request.username(), encodedPassword, role);
        User savedUser = userRepository.save(user);

        return UserResponse.from(savedUser);
    }
}
