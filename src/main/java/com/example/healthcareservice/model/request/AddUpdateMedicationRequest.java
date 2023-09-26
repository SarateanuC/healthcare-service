package com.example.healthcareservice.model.request;

import com.example.healthcareservice.enums.MedicationUnit;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddUpdateMedicationRequest {
    private String description;
    private BigDecimal dosage;
    private MedicationUnit unit;
    private LocalTime time;
}
