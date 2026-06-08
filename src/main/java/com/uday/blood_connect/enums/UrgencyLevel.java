package com.uday.blood_connect.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.uday.blood_connect.exception.InvalidEnumException;

public enum UrgencyLevel {
    NORMAL,
    URGENT,
    CRITICAL;

    @JsonCreator
    public static UrgencyLevel fromString(String value) {
        if (value == null) {
            throw new InvalidEnumException("Urgency level cannot be null");
        }

        try {
            return UrgencyLevel.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidEnumException("Invalid urgency level: " + value);
        }
    }
}
