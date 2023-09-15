package com.example.userservice.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CustomExperience {
    NOEXPERIENCE("Нет опыта"),
    LESS1YEAR("Меньше 1 года"),
    YEAR1("1 год"),
    YEAR2TO3("От 2 до 3 лет"),
    YEAR4TO6("От 4 до 6 лет"),
    OVER6YEAR("Более 6 лет");
    private final String value;

    CustomExperience(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

}

