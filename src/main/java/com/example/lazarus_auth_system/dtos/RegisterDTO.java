package com.example.lazarus_auth_system.dtos;

import com.example.lazarus_auth_system.domain.Mission;
import com.example.lazarus_auth_system.domain.enums.Role;

public record RegisterDTO(
        String username,
        String password,
        Role role,
        int missionCode
) {
}
