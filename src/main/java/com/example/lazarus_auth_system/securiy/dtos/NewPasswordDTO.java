package com.example.lazarus_auth_system.securiy.dtos;

import jakarta.validation.constraints.NotBlank;

public record NewPasswordDTO(
        @NotBlank
        String username,
        @NotBlank
        String newPassword,
        @NotBlank
        String token

) {
}
