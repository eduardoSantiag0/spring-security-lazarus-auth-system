package com.example.lazarus_auth_system.domain;


import com.example.lazarus_auth_system.domain.enums.Role;

public class User {
    private String username;
    private String password;
    private Role role;
    private Mission missionCode;

    public User(String username, String password, Role role, Mission missionCode) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.missionCode = missionCode;
    }

    public String getUsername() {
        return username;
    }


    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public Mission getMissionCode() {
        return missionCode;
    }
}
