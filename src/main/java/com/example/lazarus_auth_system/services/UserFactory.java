package com.example.lazarus_auth_system.services;

import com.example.lazarus_auth_system.securiy.dtos.RegisterDTO;
import com.example.lazarus_auth_system.infra.persistance.UserEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserFactory {

    private final PasswordEncoder passwordEncoder;

    public UserFactory(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public UserEntity createNewUser(RegisterDTO data) {
        String encryptedPassword = passwordEncoder.encode(data.password());
        return new UserEntity(data.username(), encryptedPassword, data.role(), data.missionCode());
    }
}
