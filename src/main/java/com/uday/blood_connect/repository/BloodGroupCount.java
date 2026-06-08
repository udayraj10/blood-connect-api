package com.uday.blood_connect.repository;

import com.uday.blood_connect.enums.BloodGroup;

public interface BloodGroupCount {

    BloodGroup getBloodGroup();

    Long getCount();
}
