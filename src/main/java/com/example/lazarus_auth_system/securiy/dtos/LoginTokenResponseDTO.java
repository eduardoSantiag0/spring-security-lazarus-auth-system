package com.example.lazarus_auth_system.securiy.dtos;

public record LoginTokenResponseDTO(
        String token,
        String refreshToken
){
}
