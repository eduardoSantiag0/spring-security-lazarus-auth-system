package com.example.lazarus_auth_system.services;

import com.example.lazarus_auth_system.infra.persistance.UserEntity;
import com.example.lazarus_auth_system.infra.persistance.UserRepository;
import com.example.lazarus_auth_system.securiy.dtos.NewPasswordDTO;
import com.example.lazarus_auth_system.securiy.dtos.ResetPasswordRequest;
import com.example.lazarus_auth_system.securiy.dtos.ResetPasswordResponse;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class ResetPasswordService {

    private final StringRedisTemplate redisTemplate;
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    public ResetPasswordService(StringRedisTemplate redisTemplate, UserRepository userRepository, TokenService tokenService, PasswordEncoder passwordEncoder) {
        this.redisTemplate = redisTemplate;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
    }


    public ResponseEntity<ResetPasswordResponse> processRequest(ResetPasswordRequest dto) throws Exception {
        UserEntity user = (UserEntity) userRepository.findByUsername(dto.username());
        var resetToken = tokenService.generateToken(user);

        redisTemplate.opsForValue().set("reset:token:" + user.getUsername(), resetToken, Duration.ofMinutes(15));

        return ResponseEntity.ok(new ResetPasswordResponse(resetToken));
    }


    public ResponseEntity<?> createNewPassword(NewPasswordDTO dto) {
        String key = "reset:token:" + dto.username();

        String storedToken = redisTemplate.opsForValue().get(key);

        if (!storedToken.equals(dto.token())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        UserEntity user = (UserEntity) userRepository.findByUsername(dto.username());

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }


        String encryptedNewPassword = passwordEncoder.encode(dto.newPassword());

        user.setPassword(encryptedNewPassword);
        userRepository.save(user);

        redisTemplate.delete("reset:token:" + dto.username());

        return ResponseEntity.ok("New password set!");
    }
}
