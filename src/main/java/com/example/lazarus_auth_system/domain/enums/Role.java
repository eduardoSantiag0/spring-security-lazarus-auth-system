package com.example.lazarus_auth_system.domain.enums;

public enum Role {
    ENGINEER ("ENGINEER"),
    SCIENTIST("SCIENTIST");

    private String role;

    Role(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
