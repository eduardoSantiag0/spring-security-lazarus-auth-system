package com.example.lazarus_auth_system.infra.persistance;

import com.example.lazarus_auth_system.domain.Mission;
import com.example.lazarus_auth_system.domain.enums.Role;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name="users")
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;


    private int missionCode;

    public UserEntity(String username, String password, Role role, int missionCode) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.missionCode = missionCode;
    }

    public UserEntity() {
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

//    @Override
//    public boolean isAccountNonExpired() {
//        return UserDetails.super.isAccountNonExpired();
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return UserDetails.super.isAccountNonLocked();
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return UserDetails.super.isCredentialsNonExpired();
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return UserDetails.super.isEnabled();
//    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role == Role.SCIENTIST)
            return List.of(new SimpleGrantedAuthority("ROLE_SCIENTIST"), new SimpleGrantedAuthority("ROLE_ENGINEER"));
        else
            return List.of(new SimpleGrantedAuthority("ROLE_ENGINEER"));
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public int getMissionCode() {
        return missionCode;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setMissionCode(int missionCode) {
        this.missionCode = missionCode;
    }
}
