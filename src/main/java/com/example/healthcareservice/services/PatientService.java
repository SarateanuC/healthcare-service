package com.example.healthcareservice.services;

import com.example.healthcareservice.model.entity.MedicationDbo;
import com.example.healthcareservice.model.request.AddUpdateMedicationRequest;
import com.example.healthcareservice.model.request.AddUpdatePatientRequest;
import com.example.healthcareservice.model.response.PatientFullDetailsResponse;
import com.example.healthcareservice.model.response.PatientGetResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface PatientService {
    Page<PatientGetResponse> findAllPatients(Integer size, Integer page, String criteria);

    PatientFullDetailsResponse findById(UUID patientId);

    void createPatient(AddUpdatePatientRequest request);

    void updatePatient(UUID patientId, AddUpdatePatientRequest request);

    void deletePatient(UUID patientId);

    void removeMedication(UUID patientId, UUID medicationId);

    List<MedicationDbo> getMedicationsByPatient(UUID patientId);

    void addMedicationsByPatient(UUID patientId, List<AddUpdateMedicationRequest> medications);

    void updateMedicationByPatient(UUID patientId, UUID medicationId, AddUpdateMedicationRequest request);

}
