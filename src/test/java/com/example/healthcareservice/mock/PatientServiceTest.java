package com.example.healthcareservice.mock;

import com.example.healthcareservice.model.entity.MedicationDbo;
import com.example.healthcareservice.model.entity.PatientDbo;
import com.example.healthcareservice.model.request.AddUpdateMedicationRequest;
import com.example.healthcareservice.model.request.AddUpdatePatientRequest;
import com.example.healthcareservice.repository.PatientRepository;
import com.example.healthcareservice.services.impl.PatientServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.example.healthcareservice.enums.MedicationUnit.MILLIGRAMS;
import static com.example.healthcareservice.enums.PatientGender.MALE;
import static java.util.Collections.singletonList;
import static java.util.Optional.ofNullable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientServiceImpl patientService;

    @Test
    void createPatientTest() {
        AddUpdatePatientRequest patientRequest = AddUpdatePatientRequest.builder()
                .firstname("Ion")
                .lastname("Tutunaru")
                .gender(MALE)
                .dateOfBirth(LocalDate.of(1991, 2, 15))
                .build();
        assertThatCode(() -> patientService.createPatient(patientRequest))
                .doesNotThrowAnyException();
    }

    @Test
    void updatePatientTest() {
        PatientDbo patientDbo = PatientDbo.builder()
                .firstname("test name")
                .lastname("name")
                .build();
        AddUpdatePatientRequest patientRequest = AddUpdatePatientRequest.builder()
                .firstname("Ion")
                .lastname("Tutunaru")
                .gender(MALE)
                .dateOfBirth(LocalDate.of(1991, 2, 15))
                .build();
        when(patientRepository.findById(any())).thenReturn(ofNullable(patientDbo));
        assertThatCode(() -> patientService.updatePatient(UUID.randomUUID(), patientRequest))
                .doesNotThrowAnyException();
    }

    @Test
    void findAllWithSpecifiedCriteriaTest() {
        PatientDbo patientDbo = PatientDbo.builder()
                .firstname("test name")
                .lastname("name")
                .gender(MALE)
                .build();
        when(patientRepository.findByLastnameContainingOrFirstnameContaining(any(), any()))
                .thenReturn(new PageImpl<>(List.of(patientDbo)));
        assertThat(patientService.findAllPatients(5, 0, "name").getTotalElements()).isEqualTo(1);
    }

    @Test
    void findAllWithoutCriteriaTest() {
        PatientDbo patientDbo = PatientDbo.builder()
                .firstname("test name")
                .lastname("name")
                .gender(MALE)
                .build();
        PatientDbo patient = PatientDbo.builder()
                .firstname("test")
                .lastname("test")
                .gender(MALE)
                .build();
        when(patientRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(patientDbo, patient)));
        assertThat(patientService.findAllPatients(5, 0, "").getTotalElements()).isEqualTo(2);
    }

    @Test
    void findById() {
        PatientDbo patientDbo = PatientDbo.builder()
                .firstname("test name")
                .lastname("name")
                .gender(MALE)
                .medicationDbo(List.of(MedicationDbo.builder()
                        .description("test medication")
                        .unit(MILLIGRAMS)
                        .dosage(BigDecimal.TEN)
                        .time(LocalTime.of(16, 20, 0))
                        .build()))
                .build();
        when(patientRepository.findByPatientId(any())).thenReturn(ofNullable(patientDbo));
        assertThat(patientService.findById(UUID.randomUUID()).getLastname()).isEqualTo("name");
    }

    @Test
    void deletePatientTest() {
        assertThatCode(() -> patientService.deletePatient(UUID.randomUUID()))
                .doesNotThrowAnyException();
    }

    @Test
    void addMedicationTest() {
        AddUpdateMedicationRequest medication = AddUpdateMedicationRequest.builder()
                .description("description")
                .unit(MILLIGRAMS)
                .dosage(BigDecimal.valueOf(20))
                .time(LocalTime.of(10, 30, 0))
                .build();
        PatientDbo patientDbo = PatientDbo.builder()
                .firstname("test name")
                .lastname("name")
                .gender(MALE)
                .medicationDbo(new ArrayList<>())
                .build();
        when(patientRepository.findByPatientId(any())).thenReturn(Optional.ofNullable(patientDbo));
        assertThatCode(() -> patientService.addMedicationsByPatient(UUID.randomUUID(), List.of(medication)))
                .doesNotThrowAnyException();
    }

    @Test
    void updateMedicationByPatientTest() {
        AddUpdateMedicationRequest medication = AddUpdateMedicationRequest.builder()
                .description("description")
                .unit(MILLIGRAMS)
                .dosage(BigDecimal.valueOf(20))
                .time(LocalTime.of(10, 30, 0))
                .build();
        UUID medicationId = UUID.randomUUID();
        PatientDbo patientDbo = PatientDbo.builder()
                .firstname("test name")
                .lastname("name")
                .gender(MALE)
                .medicationDbo(new ArrayList<>(singletonList(MedicationDbo.builder()
                        .id(medicationId)
                        .time(LocalTime.of(17, 5, 30))
                        .dosage(BigDecimal.ONE)
                        .unit(MILLIGRAMS)
                        .build())))
                .build();
        when(patientRepository.findByPatientId(any())).thenReturn(Optional.ofNullable(patientDbo));
        assertThatCode(() -> patientService.updateMedicationByPatient(UUID.randomUUID(), medicationId, medication))
                .doesNotThrowAnyException();
    }

    @Test
    void removeMedicationByPatientTest() {
        UUID medicationId = UUID.randomUUID();
        PatientDbo patientDbo = PatientDbo.builder()
                .firstname("test name")
                .lastname("name")
                .gender(MALE)
                .medicationDbo(new ArrayList<>(singletonList(MedicationDbo.builder()
                        .id(medicationId)
                        .time(LocalTime.of(17, 5, 30))
                        .dosage(BigDecimal.ONE)
                        .unit(MILLIGRAMS)
                        .build())))
                .build();
        when(patientRepository.findByPatientId(any())).thenReturn(Optional.ofNullable(patientDbo));
        assertThatCode(() -> patientService.removeMedication(UUID.randomUUID(), medicationId))
                .doesNotThrowAnyException();
    }

    @Test
    void getListOfMedicationByPatient() {
        when(patientRepository.findMedicationByPatientId(any())).thenReturn(List.of(MedicationDbo.builder()
                .description("test")
                .build()));
        assertThat(patientService.getMedicationsByPatient(UUID.randomUUID()).size()).isEqualTo(1);
    }
}
