package com.example.demo.review.domain;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum IsResident {
    CURRENT_RESIDENT("현재 거주"),
    PAST_RESIDENT("과거 거주"),
    NON_RESIDENT("거주 안함");

    private final String label;

    IsResident(String label) {
        this.label = label;
    }

    @JsonValue
    public String getName() {
        return this.name();
    }

    public String getLabel() {
        return label;
    }

    @JsonCreator
    public static IsResident from(String value) {
        for (IsResident i : values()) {
            if (i.name().equalsIgnoreCase(value)) {
                return i;
            }
        }
        throw new IllegalArgumentException("Unknown IsResident value: " + value);
    }
}
