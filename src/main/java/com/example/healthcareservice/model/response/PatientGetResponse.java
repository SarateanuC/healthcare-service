package com.example.healthcareservice.model.response;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PatientGetResponse {
    private String lastname;
    private String firstname;
    private String gender;
    private LocalDate dateOfBirth;
    private LocalDateTime creationDate;
    private LocalDateTime modifyDate;
}
