package com.uday.blood_connect.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.uday.blood_connect.exception.InvalidEnumException;

public enum OfferStatus {
    PENDING,
    ACCEPTED,
    DECLINED,
    COMPLETED,
    CANCELLED;

    @JsonCreator
    public static OfferStatus fromString(String status) {
        if (status == null) {
            throw new InvalidEnumException("Status cannot be null");
        }

        try {
            return OfferStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidEnumException("Invalid offer status: " + status);
        }
    }
}
