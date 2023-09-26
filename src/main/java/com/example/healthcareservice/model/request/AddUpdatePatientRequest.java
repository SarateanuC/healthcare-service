package com.example.healthcareservice.model.request;

import com.example.healthcareservice.enums.PatientGender;
import com.example.healthcareservice.validation.DateBirthValidation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddUpdatePatientRequest {
    @NotBlank
    private String lastname;
    @NotBlank
    private String firstname;
    @NotNull
    private PatientGender gender;
    @NotNull
    @DateBirthValidation
    private LocalDate dateOfBirth;
}
