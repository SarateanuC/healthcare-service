package com.example.healthcareservice.model.entity;

import com.example.healthcareservice.enums.PatientGender;
import com.example.healthcareservice.validation.DateBirthValidation;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@Builder
@With
@Table(name = "patient")
@AllArgsConstructor
@NoArgsConstructor
public class PatientDbo {
    @Id
    @GeneratedValue
    private UUID id;
    private String lastname;
    private String firstname;
    @Enumerated(STRING)
    private PatientGender gender;
    @DateBirthValidation
    private LocalDate dateOfBirth;
    @CreationTimestamp
    private LocalDateTime creationDate;
    @UpdateTimestamp
    private LocalDateTime modifyDate;
    @OneToMany(cascade = ALL, fetch = LAZY)
    @JoinColumn(name = "patient_id")
    private List<MedicationDbo> medicationDbo;
}
