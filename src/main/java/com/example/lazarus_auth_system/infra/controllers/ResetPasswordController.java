package com.example.lazarus_auth_system.infra.controllers;

import com.example.lazarus_auth_system.securiy.dtos.NewPasswordDTO;
import com.example.lazarus_auth_system.securiy.dtos.ResetPasswordRequest;
import com.example.lazarus_auth_system.securiy.dtos.ResetPasswordResponse;
import com.example.lazarus_auth_system.services.ResetPasswordService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ResetPasswordController {

    @Autowired
    private ResetPasswordService resetPasswordService;

    @PostMapping("/reset-password")
    public ResponseEntity<ResetPasswordResponse> resetPassword(@RequestBody @Validated ResetPasswordRequest username) throws Exception {
        return resetPasswordService.processRequest(username);
    }

    @PostMapping("/reset-password/confirm")
    public  ResponseEntity<?> confirmResetPassword(@RequestBody @Valid NewPasswordDTO dto) {
        return resetPasswordService.createNewPassword(dto);
    }

}
