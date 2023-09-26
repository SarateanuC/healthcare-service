package com.example.healthcareservice.enums;

public enum PatientGender {
    MALE("Male"),
    FEMALE("Female"),
    DIVERS("Divers");

    private final String value;

    PatientGender(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
