package com.example.lazarus_auth_system.infra.controllers;

import com.example.lazarus_auth_system.domain.User;
import com.example.lazarus_auth_system.infra.persistance.UserEntity;
import com.example.lazarus_auth_system.securiy.SecurityConfiguration;
import com.example.lazarus_auth_system.securiy.dtos.*;
import com.example.lazarus_auth_system.services.BlacklistService;
import com.example.lazarus_auth_system.services.TokenService;
import com.example.lazarus_auth_system.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("auth")
@Tag(name = "auth", description = "Controller to authenticate user")
@SecurityRequirement(name = SecurityConfiguration.SECURITY_HEADER)
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private BlacklistService blacklistService;


    // Criar conta
    @PostMapping("/signup")
    @Operation(summary = "Create a user")
    @ApiResponse(responseCode = "201", description = "User created")
    @ApiResponse(responseCode = "409", description = "Username already exists")
    @ApiResponse(responseCode = "404", description = "Mission code does not exist")
    public ResponseEntity<String> signup (@RequestBody @Validated RegisterDTO data) {
        return authService.saveUser(data);
    }


    // Login
    @PostMapping("/login")
    @Operation(summary = "Login User")
    @ApiResponse(responseCode = "200", description = "User login successful")
    @ApiResponse(responseCode = "401", description = "Invalid credentials")
    public ResponseEntity<?> login(@RequestBody @Validated AuthenticationDTO data, HttpServletRequest request) throws Exception {

        String ip = Optional.ofNullable(request.getHeader("X-Forwarded-For"))
                .map(h -> h.split(",")[0].trim())
                .orElse(request.getRemoteAddr());

        if (blacklistService.isBlocked(ip)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("Too many failed attempts. Please try again later.");
        }

        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(data.username(), data.password());
            var auth = this.authenticationManager.authenticate(usernamePassword);

            UserEntity user = (UserEntity) auth.getPrincipal();

            var token = tokenService.generateToken(user);
            var refreshToken = tokenService.generateRefreshToken(user);

            authService.updateRefreshToken(user, refreshToken);

            blacklistService.resetAttempts(ip);

            return ResponseEntity.ok(new LoginTokenResponseDTO(token, refreshToken));

        } catch (Exception ex) {
            blacklistService.track(ip);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password");
        }


    }

    @PostMapping("/logout")
    @Operation(summary = "Logout User")
    @ApiResponse(responseCode = "200", description = "User login successful")
    @ApiResponse(responseCode = "400", description = "Invalid or missing JWT Token")
    public ResponseEntity<?> logout(@AuthenticationPrincipal User user, HttpServletRequest request
    ) throws Exception {
        String token = tokenService.extractToken(request);

        String subject = tokenService.validateToken(token);
        if (subject.isEmpty()) {
            return ResponseEntity.badRequest().body("Empty token");
        }

        System.out.println("\n\nTOKEN: " + token);
        System.out.println("Subject: " + subject);

        long expiration = tokenService.getExpiration(token);
        blacklistService.blacklistToken(token, expiration);

        return ResponseEntity.ok("Logout realizado com sucesso para o usuário ");

    }






    @PostMapping("/update-login")
    @Operation(summary = "Update Login")
    @ApiResponse(responseCode = "200", description = "Credentials updated")
    @ApiResponse(responseCode = "400", description = "Wrong password")
    public ResponseEntity<?> updateLogin(@RequestBody ChangeLoginDTO resetPasswordDTO, @AuthenticationPrincipal UserEntity user) {
        return authService.changeCredentials(resetPasswordDTO, user);

    }

    @PostMapping("/update-token")
    @Operation(summary = "Update user's token")
    @ApiResponse(responseCode = "200", description = "Refresh Token updated")
    @ApiResponse(responseCode = "400", description = "User not found or invalid token provided")
    public ResponseEntity<?> updateTokens(@Valid @RequestBody DadosRefreshToken dados) throws Exception {

        var refreshToken = dados.refreshToken();

        Long idUser = tokenService.validateRefreshTokenAndGetId(refreshToken);
        Optional<UserEntity> user = authService.loadUserById(idUser);

        if (user.isPresent()) {
            String newAccessToken = tokenService.generateToken(user.get());
            String newRefreshToken = tokenService.generateRefreshToken(user.get());
            authService.updateRefreshToken(user.get(), newRefreshToken);

            return ResponseEntity.ok(new LoginTokenResponseDTO(newAccessToken, newRefreshToken));
        }

        return ResponseEntity.badRequest().body("User not found or invalid token provided");

    }


}
