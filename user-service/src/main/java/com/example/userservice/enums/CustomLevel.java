package com.example.userservice.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CustomLevel {
    SECONDARY("Среднее"),
    SPECIALIZEDSECONDARY("Среднее специальное"),
    INCOMPLETEHIGHER("Неоконченное высшее"),
    HIGHER("Высшее"),
    BACHELORDEGREE("Бакалавр"),
    MASTERDEGREE("Магистр");

    private final String value;

    CustomLevel(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
