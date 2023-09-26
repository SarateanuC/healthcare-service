package com.example.healthcareservice.enums;

public enum MedicationUnit {
    GRAMS("Grams"),
    MILLIGRAMS("Milligrams"),
    TABLET("Tablet");
    private final String value;

    MedicationUnit(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
