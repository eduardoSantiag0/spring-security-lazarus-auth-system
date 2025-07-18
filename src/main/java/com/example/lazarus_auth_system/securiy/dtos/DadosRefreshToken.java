package com.example.lazarus_auth_system.securiy.dtos;

import jakarta.validation.constraints.NotBlank;

public record DadosRefreshToken(@NotBlank String refreshToken) {

}
