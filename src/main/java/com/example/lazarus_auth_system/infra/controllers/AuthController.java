package com.example.lazarus_auth_system.infra.controllers;

import com.example.lazarus_auth_system.dtos.AuthenticationDTO;
import com.example.lazarus_auth_system.dtos.LoginTokenResponseDTO;
import com.example.lazarus_auth_system.dtos.RegisterDTO;
import com.example.lazarus_auth_system.infra.persistance.UserEntity;
import com.example.lazarus_auth_system.securiy.TokenService;
import com.example.lazarus_auth_system.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;


    // Criar conta
    @PostMapping("/signup")
    public ResponseEntity<String> signup (@RequestBody @Validated RegisterDTO data) {
        return authService.saveUser(data);
    }


    // Login
    @PostMapping("/login")
    public ResponseEntity<LoginTokenResponseDTO> login(@RequestBody @Validated AuthenticationDTO data) throws Exception {

        var usernamePassword = new UsernamePasswordAuthenticationToken(data.username(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((UserEntity) auth.getPrincipal());

        return ResponseEntity.ok(new LoginTokenResponseDTO(token));

    }


    //todo
//    @PostMapping("/alterar-login")
//    public ResponseEntity<String> alterarLogin(@RequestBody @Validated AuthenticationDTO data) {
//    }

    //todo
//    @PostMapping("/esueci-minha-senha")




}
