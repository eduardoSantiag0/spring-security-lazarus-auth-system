package com.example.lazarus_auth_system.dtos;

public record LoginTokenResponseDTO(
        String token,
        String refreshToken
){
}
