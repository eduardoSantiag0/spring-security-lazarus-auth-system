package com.example.lazarus_auth_system.dtos;

import jakarta.validation.constraints.NotBlank;

public record DadosRefreshToken(@NotBlank String refreshToken) {

}
