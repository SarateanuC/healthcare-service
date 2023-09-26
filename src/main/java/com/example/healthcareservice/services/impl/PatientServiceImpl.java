package com.example.healthcareservice.services.impl;

import com.example.healthcareservice.exception.PatientNotFoundException;
import com.example.healthcareservice.model.entity.MedicationDbo;
import com.example.healthcareservice.model.entity.PatientDbo;
import com.example.healthcareservice.model.request.AddUpdateMedicationRequest;
import com.example.healthcareservice.model.request.AddUpdatePatientRequest;
import com.example.healthcareservice.model.response.MedicationGetResponse;
import com.example.healthcareservice.model.response.PatientFullDetailsResponse;
import com.example.healthcareservice.model.response.PatientGetResponse;
import com.example.healthcareservice.repository.PatientRepository;
import com.example.healthcareservice.services.PatientService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {
    private final PatientRepository patientRepository;

    @Override
    public Page<PatientGetResponse> findAllPatients(Integer size, Integer page, String criteria) {
        val pageable = PageRequest.of(
                page,
                size,
                Sort.by("firstname").ascending()
                        .and(Sort.by("lastname").ascending())
        );

        return ofNullable(criteria)
                .map(e -> patientRepository.findByLastnameContainingOrFirstnameContaining(
                                criteria,
                                pageable
                        )
                )
                .orElseGet(() -> patientRepository.findAll(pageable))
                .map(this::mapPatientToDto);
    }

    @Override
    public PatientFullDetailsResponse findById(UUID patientId) {
        return mapFullPatientDetailsToDto(patientRepository.findByPatientId(patientId)
                .orElseThrow(PatientNotFoundException::new));
    }

    @Override
    public void createPatient(AddUpdatePatientRequest request) {
        patientRepository.save(PatientDbo.builder()
                .lastname(request.getLastname())
                .firstname(request.getFirstname())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .build());
    }

    @Override
    public void updatePatient(UUID patientId, AddUpdatePatientRequest request) {
        val patientDbo = patientRepository.findById(patientId)
                .map(e -> e.withFirstname(request.getFirstname())
                        .withLastname(request.getLastname())
                        .withGender(request.getGender())
                        .withDateOfBirth(request.getDateOfBirth()))
                .orElseThrow(PatientNotFoundException::new);
        patientRepository.save(patientDbo);
    }

    @Override
    public void deletePatient(UUID patientId) {
        patientRepository.deleteById(patientId);
    }

    @Override
    public void removeMedication(UUID patientId, UUID medicationId) {
        val patientDbo = patientRepository.findByPatientId(patientId)
                .orElseThrow(PatientNotFoundException::new);
        patientDbo.getMedicationDbo().removeIf(value -> value.getId().equals(medicationId));
        patientRepository.save(patientDbo);
    }

    @Override
    public List<MedicationDbo> getMedicationsByPatient(UUID patientId) {
        return patientRepository.findMedicationByPatientId(patientId);
    }

    @Override
    public void addMedicationsByPatient(UUID patientId, List<AddUpdateMedicationRequest> medications) {
        val patient = patientRepository.findByPatientId(patientId)
                .orElseThrow(PatientNotFoundException::new);
        val medicationDbos = medications.stream()
                .map(e -> MedicationDbo.builder()
                        .unit(e.getUnit())
                        .description(e.getDescription())
                        .dosage(e.getDosage())
                        .time(e.getTime())
                        .build())
                .toList();
        patient.getMedicationDbo().addAll(medicationDbos);
        patientRepository.save(patient);
    }

    @Override
    public void updateMedicationByPatient(UUID patientId, UUID medicationId, AddUpdateMedicationRequest request) {
        val patientDbo = patientRepository.findByPatientId(patientId)
                .orElseThrow(PatientNotFoundException::new);
        val medicationDbos = patientDbo.getMedicationDbo();
        val medication = medicationDbos.stream()
                .filter(e -> e.getId().equals(medicationId))
                .findFirst()
                .orElseThrow();
        val index = medicationDbos.indexOf(medication);
        val medicationDbo = medication.withDosage(request.getDosage())
                .withDescription(request.getDescription())
                .withTime(request.getTime())
                .withUnit(request.getUnit());
        medicationDbos.set(index, medicationDbo);
        patientRepository.save(patientDbo);
    }

    private PatientGetResponse mapPatientToDto(PatientDbo patientDbo) {
        return PatientGetResponse.builder()
                .firstname(patientDbo.getFirstname())
                .lastname(patientDbo.getLastname())
                .creationDate(patientDbo.getCreationDate())
                .dateOfBirth(patientDbo.getDateOfBirth())
                .gender(patientDbo.getGender().getValue())
                .modifyDate(patientDbo.getModifyDate())
                .build();
    }

    private MedicationGetResponse mapMedicationToDto(MedicationDbo medicationDbo) {
        return MedicationGetResponse.builder()
                .description(medicationDbo.getDescription())
                .dosage(medicationDbo.getDosage())
                .creationDate(medicationDbo.getCreationDate())
                .unit(medicationDbo.getUnit().getValue())
                .time(medicationDbo.getTime())
                .modifyDate(medicationDbo.getModifyDate())
                .build();
    }

    private PatientFullDetailsResponse mapFullPatientDetailsToDto(PatientDbo patientDbo) {
        val medicationList = patientDbo.getMedicationDbo()
                .stream()
                .map(this::mapMedicationToDto)
                .toList();
        return PatientFullDetailsResponse.builder()
                .firstname(patientDbo.getFirstname())
                .lastname(patientDbo.getLastname())
                .creationDate(patientDbo.getCreationDate())
                .gender(patientDbo.getGender().getValue())
                .modifyDate(patientDbo.getModifyDate())
                .medications(medicationList)
                .dateOfBirth(patientDbo.getDateOfBirth())
                .build();
    }
}
