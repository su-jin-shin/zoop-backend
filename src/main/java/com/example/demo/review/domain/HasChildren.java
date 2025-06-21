package com.example.demo.review.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum HasChildren {
    HAS_CHILDREN("자녀 있음"),
    NON_CHILDREN("자녀 없음");

    private final String label;

    HasChildren(String label) {
        this.label = label;
    }

    @JsonValue
    public String getName() {
        return this.name(); // 프론트로 보낼 값: HAS_CHILDREN, NON_CHILDREN
    }

    public String getLabel() {
        return label; // UI에 보여줄 값
    }

    @JsonCreator
    public static HasChildren from(String value) {
        for (HasChildren h : HasChildren.values()) {
            if (h.name().equalsIgnoreCase(value)) {
                return h;
            }
        }
        throw new IllegalArgumentException("Unknown HasChildren value: " + value);
    }
}
