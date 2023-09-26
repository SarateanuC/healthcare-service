package com.example.healthcareservice.repository;

import com.example.healthcareservice.model.entity.MedicationDbo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MedicationRepository extends JpaRepository<MedicationDbo, UUID> {
}
