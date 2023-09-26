package com.example.healthcareservice.model.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MedicationGetResponse {
    private String description;
    private BigDecimal dosage;
    private String unit;
    private LocalTime time;
    private LocalDateTime creationDate;
    private LocalDateTime modifyDate;
}
