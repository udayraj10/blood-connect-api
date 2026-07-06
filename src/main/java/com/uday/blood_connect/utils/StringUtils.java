package com.uday.blood_connect.utils;

public final class StringUtils {
    
    private StringUtils() {}

    public static String capitalizeFirstOnly(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
}