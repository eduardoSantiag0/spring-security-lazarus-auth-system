package com.example.lazarus_auth_system.infra.persistance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserDetails findByUsername(String username);
    Optional<UserEntity> findById(Long id);
}
