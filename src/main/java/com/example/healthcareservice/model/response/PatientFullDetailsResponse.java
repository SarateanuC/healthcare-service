package com.example.healthcareservice.model.response;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PatientFullDetailsResponse {
    private String lastname;
    private String firstname;
    private String gender;
    private LocalDate dateOfBirth;
    private LocalDateTime creationDate;
    private LocalDateTime modifyDate;
    private List<MedicationGetResponse> medications;
}
