package com.example.healthcareservice.model.entity;

import com.example.healthcareservice.enums.MedicationUnit;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

import static jakarta.persistence.EnumType.STRING;

@Entity
@Getter
@Setter
@Builder
@Table(name = "medication")
@With
@AllArgsConstructor
@NoArgsConstructor
public class MedicationDbo {
    @Id
    @GeneratedValue
    private UUID id;
    @Size(min = 3, message = "{validation.name.size.too_short}")
    private String description;
    @Min(0)
    private BigDecimal dosage;
    @Enumerated(STRING)
    private MedicationUnit unit;
    private LocalTime time;
    @CreationTimestamp
    private LocalDateTime creationDate;
    @UpdateTimestamp
    private LocalDateTime modifyDate;


}
