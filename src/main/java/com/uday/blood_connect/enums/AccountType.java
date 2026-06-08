package com.uday.blood_connect.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.uday.blood_connect.exception.InvalidEnumException;

public enum AccountType {

    INDIVIDUAL, ORGANIZATION;

    @JsonCreator
    public static AccountType fromString(String value) {
        if (value == null) {
            throw new InvalidEnumException("Account type cannot be null");
        }

        try {
            return AccountType.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            // If the value doesn't match any enum constant, we can handle it
            throw new InvalidEnumException("Invalid account type: " + value);
        }
    }
}
