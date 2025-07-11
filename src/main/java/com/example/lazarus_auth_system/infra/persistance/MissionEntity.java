package com.example.lazarus_auth_system.infra.persistance;

import com.example.lazarus_auth_system.domain.User;
import com.example.lazarus_auth_system.domain.enums.Classification;
import com.example.lazarus_auth_system.domain.enums.MissionStatus;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "Missions")
public class MissionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mission_id", nullable = false)
    private Long id;


    @Column(name = "mission_code", nullable = false, unique = true)
    private int missionCode;

    @Column(name = "description", nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "classification", nullable = false)
    private Classification classification;


    @Enumerated(EnumType.STRING)
    @Column(name = "mission_status", nullable = false)
    private MissionStatus missionStatus;

    @Column(name = "planet_name", nullable = false)
    private String planetName;


    public MissionEntity() {
    }


    public MissionEntity(int missionCode, String description, Classification classification, MissionStatus missionStatus, String planetName) {
        this.missionCode = missionCode;
        this.description = description;
        this.classification = classification;
        this.missionStatus = missionStatus;
        this.planetName = planetName;
    }

    public Long getId() {
        return id;
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setMissionCode(int missionCode) {
        this.missionCode = missionCode;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setClassification(Classification classification) {
        this.classification = classification;
    }

    public void setMissionStatus(MissionStatus missionStatus) {
        this.missionStatus = missionStatus;
    }

    public void setPlanetName(String planetName) {
        this.planetName = planetName;
    }
}
