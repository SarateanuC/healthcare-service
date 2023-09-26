package com.example.healthcareservice.repository;

import com.example.healthcareservice.model.entity.MedicationDbo;
import com.example.healthcareservice.model.entity.PatientDbo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PatientRepository extends JpaRepository<PatientDbo, UUID> {

    @Query("Select p from PatientDbo p where p.firstname like %:value% or p.lastname like %:value%")
    Page<PatientDbo> findByLastnameContainingOrFirstnameContaining(@Param("value") String value, Pageable pageable);

    @Query("Select p.medicationDbo from PatientDbo p inner join p.medicationDbo where p.id =:id")
    List<MedicationDbo> findMedicationByPatientId(@Param("id") UUID id);

    @Query("Select p from PatientDbo p join fetch p.medicationDbo where p.id =:id")
    Optional<PatientDbo> findByPatientId(@Param("id") UUID id);
}
