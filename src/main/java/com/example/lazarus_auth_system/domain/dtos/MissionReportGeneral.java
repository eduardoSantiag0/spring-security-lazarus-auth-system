package com.example.lazarus_auth_system.domain.dtos;

import com.example.lazarus_auth_system.domain.enums.Classification;

public record MissionReportGeneral(int missionCode,
                                   Classification classification,
                                   String planetName) {
}
