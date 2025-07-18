package com.example.lazarus_auth_system.domain;

import com.example.lazarus_auth_system.domain.enums.Classification;
import com.example.lazarus_auth_system.domain.enums.MissionStatus;

public record Mission (
    int missionCode,
    String description,
    Classification classification,
    MissionStatus missionStatus,
    String planetName
) {

}
