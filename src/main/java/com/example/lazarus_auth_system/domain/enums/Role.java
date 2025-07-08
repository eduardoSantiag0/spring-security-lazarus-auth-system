package com.example.lazarus_auth_system.domain.enums;

public enum Role {

    ENGINEER ("ENGINEER"),
    // GET api/mission

    SCIENTIST("SCIENTIST");
    // POST api/mission
    // GET api/mission/confidential

    private String role;

    Role(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
