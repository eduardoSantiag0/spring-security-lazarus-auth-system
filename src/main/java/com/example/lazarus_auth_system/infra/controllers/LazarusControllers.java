package com.example.lazarus_auth_system.infra.controllers;

import com.example.lazarus_auth_system.dtos.mission_reports.MissionUpdateDTO;
import com.example.lazarus_auth_system.infra.persistance.UserEntity;
import com.example.lazarus_auth_system.securiy.SecurityConfiguration;
import com.example.lazarus_auth_system.services.AuthService;
import com.example.lazarus_auth_system.services.MissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/lazarus")
@RestController
@Tag(name = "lazarus", description = "Controller to get User details and mission information")
@SecurityRequirement(name = SecurityConfiguration.SECURITY_HEADER)
public class LazarusControllers {

    @Autowired
    private AuthService authService;


    @Autowired
    private MissionService missionService;

    @GetMapping("/users/me")
    @Operation(summary = "Get User credentials")
    @ApiResponse(responseCode = "200", description = "User found")
    @ApiResponse(responseCode = "401", description = "User not authenticated")
    public ResponseEntity<String> getCredentials() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authenticated");
        }

        var principal = (UserDetails) authentication.getPrincipal();

        return ResponseEntity.ok(principal.getUsername());

    }

    @GetMapping("/mission")
    @Operation(summary = "Get mission information",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Mission information returned"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized")
            })
    public ResponseEntity<?> getMissionInformation(@AuthenticationPrincipal UserEntity principal) {
        return missionService.getMissionInformation(principal);
    }


    @GetMapping("/mission/confidential")
    @Operation(summary = "Update mission information (SCIENTIST only)",
            responses = { @ApiResponse(responseCode = "200", description = "Mission updated"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            })
    public ResponseEntity<?> getConfidentialInformation(@AuthenticationPrincipal UserEntity user) {
        return missionService.getConfidentialInformation(user);

    }


    @PatchMapping("/mission")
    @Operation(summary = "Update mission information (SCIENTIST only)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Mission updated"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            })
    public ResponseEntity<?> updateMissionInformation (@RequestBody MissionUpdateDTO updateDTO, @AuthenticationPrincipal UserEntity user) {
        return missionService.updateMissionStatus(updateDTO, user);
    }


}
