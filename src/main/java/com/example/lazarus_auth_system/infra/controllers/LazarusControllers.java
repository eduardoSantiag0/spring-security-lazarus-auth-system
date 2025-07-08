package com.example.lazarus_auth_system.infra.controllers;

import com.example.lazarus_auth_system.domain.enums.Role;
import com.example.lazarus_auth_system.dtos.mission_reports.MissionReportConfidential;
import com.example.lazarus_auth_system.dtos.mission_reports.MissionReportGeneral;
import com.example.lazarus_auth_system.infra.persistance.MissionEntity;
import com.example.lazarus_auth_system.infra.persistance.MissionRepository;
import com.example.lazarus_auth_system.infra.persistance.UserEntity;
import com.example.lazarus_auth_system.services.AuthService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequestMapping("/lazarus")
@RestController
public class LazarusControllers {

    @Autowired
    private AuthService service;

    @Autowired
    private MissionRepository missionRepository;

    //todo Return credentials
    @GetMapping("/users/me")
    public ResponseEntity<String> getCredentials() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authenticated");
        }

        var principal = (UserDetails) authentication.getPrincipal();

        return ResponseEntity.ok(principal.getUsername());

    }

    //todo Apenas para ENGINEER ou SCIENTIST
    @GetMapping("/mission")
    public ResponseEntity<?> getMissionInformation() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authenticated");
        }

        var principal = (UserEntity) authentication.getPrincipal();

        Optional<MissionEntity> mission = missionRepository.findByMissionCode(principal.getMissionCode());

        if (mission.isPresent()) {
            MissionEntity activeMission = mission.get();

            MissionReportGeneral confidentialReport = new MissionReportGeneral(
                    activeMission.getMissionCode(),
                    activeMission.getClassification(),
                    activeMission.getPlanetName());

            return ResponseEntity.ok(confidentialReport);

        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Mission not found for user");

    }


    //todo Apenas para SCIENTIST
//    @PutMapping("/missions")
//    public ResponseEntity<?> updateMissionInformation (@RequestBody String newStatus) {}


    //todo Apenas para SCIENTIST, informações completas se o mission for confidential
//    @PreAuthorize("hasRole('SCIENTIST')")
    @GetMapping("/mission/confidential")
    public ResponseEntity<?> getConfidentialInformation(@AuthenticationPrincipal UserEntity user) {

        Optional<MissionEntity> mission = missionRepository.findByMissionCode(user.getMissionCode());

        if (mission.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Mission not found for user");
        }

        MissionEntity activeMission = mission.get();

        MissionReportConfidential confidentialReport = new MissionReportConfidential(
                activeMission.getMissionCode(),
                activeMission.getDescription(),
                activeMission.getClassification(),
                activeMission.getMissionStatus(),
                activeMission.getPlanetName()
        );

        System.out.println("User role: " + user.getRole());
        System.out.println("Authorities: " + user.getAuthorities());


        return ResponseEntity.ok(confidentialReport);
    }

}
