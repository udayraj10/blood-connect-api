package com.uday.blood_connect.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.uday.blood_connect.exception.InvalidEnumException;

public enum RequestStatus {
    OPEN,
    FULFILLED,
    CANCELLED;

    @JsonCreator
    public static RequestStatus fromString(String status) {
        if (status == null) {
            throw new InvalidEnumException("Status cannot be null");
        }

        try {
            return RequestStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidEnumException("Invalid status: " + status);
        }
    }
}