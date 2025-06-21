package com.example.demo.review.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SummaryType {
    GOOD("장점"),
    BAD("단점"),
    TRA("교통"),
    EDU("교육"),
    HEL("의료"),
    LOC("위치");

    private final String label;

    SummaryType(String label) {
        this.label = label;
    }

    @JsonValue
    public String getName() {
        return name();
    }

    public String getLabel() {
        return label;
    }

    @JsonCreator
    public static SummaryType from(String value) {
        for (SummaryType s : values()) {
            if (s.name().equalsIgnoreCase(value)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Unknown SummaryType value: " + value);
    }
}
