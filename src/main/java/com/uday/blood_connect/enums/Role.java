package com.uday.blood_connect.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.uday.blood_connect.exception.InvalidEnumException;

public enum Role {

    USER,
    ADMIN;

    @JsonCreator
    public static Role fromString(String value) {
        if (value == null) {
            throw new InvalidEnumException("Role cannot be null");
        }

        try {
            return Role.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            // If the value doesn't match any enum constant, we can handle it gracefully
            throw new InvalidEnumException("Invalid role: " + value);
        }
    }
}
