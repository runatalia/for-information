package com.example.userservice.enums;

public enum CustomRole {
    CANDIDATE("CANDIDATE"),
    ADMIN("ADMIN"),
    RECRUITER("RECRUITER"),
    TECH_SPECIALIST("TECH_SPECIALIST");
    private final String value;

    CustomRole(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
