package com.example.lazarus_auth_system.domain.dtos;

import com.example.lazarus_auth_system.domain.enums.Classification;
import com.example.lazarus_auth_system.domain.enums.MissionStatus;

import java.util.Optional;

public record MissionUpdateDTO(
        Optional<MissionStatus> missionStatus,
        Optional<String> description,
        Optional<Classification> classification,
        Optional<String> planetName
) {}