package com.identity.dto;

import com.identity.entity.User;
import java.util.UUID;

public record UserResponse(
    UUID id,
    String username,
    String role
) {
    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getUsername(), user.getRole());
    }
}
