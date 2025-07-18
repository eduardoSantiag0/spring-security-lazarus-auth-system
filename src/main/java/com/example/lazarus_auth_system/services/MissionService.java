package com.example.lazarus_auth_system.services;

import com.example.lazarus_auth_system.domain.dtos.MissionReportConfidential;
import com.example.lazarus_auth_system.domain.dtos.MissionReportGeneral;
import com.example.lazarus_auth_system.domain.dtos.MissionUpdateDTO;
import com.example.lazarus_auth_system.infra.persistance.MissionEntity;
import com.example.lazarus_auth_system.infra.persistance.MissionRepository;
import com.example.lazarus_auth_system.infra.persistance.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MissionService {


    @Autowired
    private MissionRepository missionRepository;

    public ResponseEntity<?> updateMissionStatus(MissionUpdateDTO updateDTO, UserEntity user) {

        Optional<MissionEntity> optionalMission = missionRepository.findByMissionCode(user.getMissionCode());
        if (optionalMission.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Mission not found for user");
        }

        MissionEntity mission = optionalMission.get();

        if (updateDTO.missionStatus().isPresent()) mission.setMissionStatus(updateDTO.missionStatus().get());
        if (updateDTO.description().isPresent()) mission.setDescription(updateDTO.description().get());
        if (updateDTO.classification().isPresent()) mission.setClassification(updateDTO.classification().get());
        if (updateDTO.planetName().isPresent()) mission.setPlanetName(updateDTO.planetName().get());


        missionRepository.save(mission);

        return ResponseEntity.ok("Mission Updated");

    }

    public ResponseEntity<?> getMissionInformation (UserEntity user) {

        Optional<MissionEntity> mission = missionRepository.findByMissionCode(user.getMissionCode());

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

    public ResponseEntity<?> getConfidentialInformation(UserEntity user) {

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
