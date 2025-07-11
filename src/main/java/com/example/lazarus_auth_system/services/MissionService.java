package com.example.lazarus_auth_system.services;

import com.example.lazarus_auth_system.dtos.mission_reports.MissionReportConfidential;
import com.example.lazarus_auth_system.dtos.mission_reports.MissionUpdateDTO;
import com.example.lazarus_auth_system.infra.persistance.MissionEntity;
import com.example.lazarus_auth_system.infra.persistance.MissionRepository;
import com.example.lazarus_auth_system.infra.persistance.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

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

}
