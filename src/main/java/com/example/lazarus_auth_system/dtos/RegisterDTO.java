package com.example.lazarus_auth_system.dtos;

import com.example.lazarus_auth_system.domain.Mission;
import com.example.lazarus_auth_system.domain.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;

public record RegisterDTO(
        @Schema(description = "Username for the user", example = "sgobbi")
        String username,
        @Schema(description = "Password for the user", example = "sgobbi123")
        String password,
        @Schema(description = "Permission based role", example = "SCIENTIST")
        Role role,
        @Schema(description = "Mission code", example = "12345")
        int missionCode
) {
}
