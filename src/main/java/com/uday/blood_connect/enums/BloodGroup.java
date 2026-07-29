package com.uday.blood_connect.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.uday.blood_connect.exception.InvalidEnumException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public enum BloodGroup {
    A_POS("A+"),
    A_NEG("A-"),
    B_POS("B+"),
    B_NEG("B-"),
    AB_POS("AB+"),
    AB_NEG("AB-"),
    O_POS("O+"),
    O_NEG("O-");

    private final String value;

    BloodGroup(String value) {
        this.value = value;
    }

    @JsonValue
    public String getDisplayName() {
        return value;
    }

    @JsonCreator
    public static BloodGroup fromString(String value) {
        if (value == null) {
            throw new InvalidEnumException("Blood group cannot be null");
        }

        for (BloodGroup group : BloodGroup.values()) {
            if (group.value.equalsIgnoreCase(value.trim())) {
                return group;
            }
        }

        throw new InvalidEnumException("Invalid blood group: " + value);
    }

    public static List<BloodGroup> resolveMatchingGroups(String input) {
        if (input == null || input.isBlank()) {
            return Collections.emptyList();
        }

        String cleanInput = input.trim().toUpperCase();

        return Arrays.stream(values())
                .filter(enumVal -> {

                    if (enumVal.value.equalsIgnoreCase(cleanInput)) {
                        return true;
                    }

                    String baseGroup = enumVal.value.replaceAll("[+-]", "");
                    return baseGroup.equalsIgnoreCase(cleanInput);
                })
                .collect(Collectors.toList());
    }
}
