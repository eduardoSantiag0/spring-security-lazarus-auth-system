package com.example.lazarus_auth_system.securiy.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

public record AuthenticationDTO(
        @Schema(example = "sgobbi")
        String username,
        @Schema(example = "sgobbi123")
        String password
) {}
