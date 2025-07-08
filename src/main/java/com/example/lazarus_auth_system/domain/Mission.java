package com.example.lazarus_auth_system.domain;

import com.example.lazarus_auth_system.domain.enums.Classification;
import com.example.lazarus_auth_system.domain.enums.MissionStatus;

import java.util.List;

public class Mission {
    private final int missionCode;
    private String description;
    private Classification classification;
    private MissionStatus missionStatus;
    private String planetName;

    public Mission(int missionCode, String description, Classification classification,  MissionStatus missionStatus, String planetName) {
        this.missionCode = missionCode;
        this.description = description;
        this.classification = classification;
        this.missionStatus = missionStatus;
        this.planetName = planetName;
    }

    public int getMissionCode() {
        return missionCode;
    }

    public String getDescription() {
        return description;
    }

    public Classification getClassification() {
        return classification;
    }

    public MissionStatus getMissionStatus() {
        return missionStatus;
    }

    public String getPlanetName() {
        return planetName;
    }


}
