package com.example.lazarus_auth_system.services;

import com.example.lazarus_auth_system.dtos.ChangeLoginDTO;
import com.example.lazarus_auth_system.infra.persistance.UserEntity;
import com.example.lazarus_auth_system.infra.persistance.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void updateUserCredentials(ChangeLoginDTO resetPasswordDTO, UserEntity user) {
        // Atualizar conta
        if(resetPasswordDTO.newPassword().isPresent())
            user.setPassword(passwordEncoder.encode(resetPasswordDTO.newPassword().get()));
        if(resetPasswordDTO.newUsername().isPresent())
            user.setUsername(resetPasswordDTO.newUsername().get());

        // Salvar
        userRepository.save(user);

    }

}
