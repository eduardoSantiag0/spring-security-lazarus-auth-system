package com.example.lazarus_auth_system.infra.persistance;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MissionRepository extends JpaRepository<MissionEntity, Long> {
    Optional<MissionEntity> findByMissionCode(int missionCode);
}
