package com.uday.blood_connect.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.uday.blood_connect.exception.InvalidEnumException;

public enum BloodGroup {
    A_POS("A+"),
    A_NEG("A-"),
    B_POS("B+"),
    B_NEG("B-"),
    AB_POS("AB+"),
    AB_NEG("AB-"),
    O_POS("O+"),
    O_NEG("O-");

    private final String displayName;

    BloodGroup(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    @JsonCreator
    public static BloodGroup fromString(String value) {
        if (value == null) {
            throw new InvalidEnumException("Blood group cannot be null");
        }

        for (BloodGroup group : BloodGroup.values()) {
            if (group.displayName.equalsIgnoreCase(value.trim())) {
                return group;
            }
        }

        throw new InvalidEnumException("Invalid blood group: " + value);
    }
}
