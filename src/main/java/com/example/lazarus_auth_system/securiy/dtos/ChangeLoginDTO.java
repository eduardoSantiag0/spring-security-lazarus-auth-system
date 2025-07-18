package com.example.lazarus_auth_system.securiy.dtos;

import java.util.Optional;

public record ChangeLoginDTO(
        String currentPassword,
        Optional<String> newUsername,
        Optional<String> newPassword
) {
}