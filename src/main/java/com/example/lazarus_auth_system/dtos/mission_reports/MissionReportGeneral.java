package com.example.lazarus_auth_system.dtos.mission_reports;

import com.example.lazarus_auth_system.domain.enums.Classification;
import com.example.lazarus_auth_system.domain.enums.MissionStatus;

public record MissionReportGeneral(int missionCode,
                                   Classification classification,
                                   String planetName) {
}
