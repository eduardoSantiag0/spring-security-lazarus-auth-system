package com.example.lazarus_auth_system.dtos;

import com.example.lazarus_auth_system.domain.enums.Role;

public record UserDTO(
        String username,
        String password,
        Role role,
        int missionCode
) {
}
