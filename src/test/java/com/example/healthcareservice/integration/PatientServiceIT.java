package com.example.healthcareservice.integration;

import com.example.healthcareservice.container.PostgresContainer;
import com.example.healthcareservice.exception.MedicationNotFoundException;
import com.example.healthcareservice.exception.PatientNotFoundException;
import com.example.healthcareservice.model.entity.MedicationDbo;
import com.example.healthcareservice.model.entity.PatientDbo;
import com.example.healthcareservice.model.request.AddUpdateMedicationRequest;
import com.example.healthcareservice.model.request.AddUpdatePatientRequest;
import com.example.healthcareservice.model.response.PatientFullDetailsResponse;
import com.example.healthcareservice.repository.MedicationRepository;
import com.example.healthcareservice.repository.PatientRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.example.healthcareservice.enums.MedicationUnit.MILLIGRAMS;
import static com.example.healthcareservice.enums.PatientGender.MALE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PatientServiceIT extends PostgresContainer {
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private MedicationRepository medicationRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void createPatientTest() throws Exception {
        AddUpdatePatientRequest patientRequest = AddUpdatePatientRequest.builder()
                .firstname("Ion")
                .lastname("Tutunaru")
                .gender(MALE)
                .dateOfBirth(LocalDate.of(1991, 2, 15))
                .build();

        mockMvc.perform(post("/api/patient/create")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patientRequest)))
                .andExpect(status().isOk());
        assertEquals(2, patientRepository.findAll().size());
    }

    @Test
    void createPatientValidationFailTest() throws Exception {
        AddUpdatePatientRequest patientRequest = AddUpdatePatientRequest.builder()
                .firstname("Ion")
                .lastname("Tutunaru")
                .gender(MALE)
                .dateOfBirth(LocalDate.of(2020, 2, 15))
                .build();

        mockMvc.perform(post("/api/patient/create")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patientRequest)))
                .andExpect(status().isBadRequest());
        assertEquals(1, patientRepository.findAll().size());
    }

    @Test
    void updatePatientTest() throws Exception {
        AddUpdatePatientRequest patientRequest = AddUpdatePatientRequest.builder()
                .firstname("Ion")
                .lastname("Tutunaru")
                .gender(MALE)
                .dateOfBirth(LocalDate.of(1991, 2, 15))
                .build();

        mockMvc.perform(put("/api/patient/update")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patientRequest))
                        .param("id", "7fe14f6e-5bd6-11ee-8c99-0242ac120002"))
                .andExpect(status().isOk());
        List<PatientDbo> patientDboList = patientRepository.findAll();
        assertEquals(1, patientDboList.size());
        assertEquals("Ion", patientDboList.get(0).getFirstname());
    }

    @Test
    void updatePatientNotFoundTest() throws Exception {
        AddUpdatePatientRequest patientRequest = AddUpdatePatientRequest.builder()
                .firstname("Ion")
                .lastname("Tutunaru")
                .gender(MALE)
                .dateOfBirth(LocalDate.of(1991, 2, 15))
                .build();

        mockMvc.perform(put("/api/patient/update")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patientRequest))
                        .param("id", "7fe14f6e-5bd6-11ee-8c99-0242ac120005"))
                .andExpect(status().isBadRequest());
        List<PatientDbo> patientDboList = patientRepository.findAll();
        assertEquals(1, patientDboList.size());
        assertEquals("firstname", patientDboList.get(0).getFirstname());
    }

    @Test
    void deletePatientTest() throws Exception {
        mockMvc.perform(delete("/api/patient/delete")
                        .contentType(APPLICATION_JSON)
                        .param("id", "7fe14f6e-5bd6-11ee-8c99-0242ac120002"))
                .andExpect(status().isOk());
        assertEquals(0, patientRepository.findAll().size());
    }

    @Test
    void removeMedicationTest() throws Exception {
        mockMvc.perform(delete("/api/patient/medication-remove")
                        .contentType(APPLICATION_JSON)
                        .param("patientId", "7fe14f6e-5bd6-11ee-8c99-0242ac120002")
                        .param("medicationId", "7fe1528e-5bd6-11ee-8c99-0242ac120004"))
                .andExpect(status().isOk());
        List<MedicationDbo> medicationDbos =
                patientRepository.findByPatientId(UUID.fromString("7fe14f6e-5bd6-11ee-8c99-0242ac120002"))
                        .map(PatientDbo::getMedicationDbo)
                        .orElseThrow(PatientNotFoundException::new);
        assertEquals(1, medicationDbos.size());
    }

    @Test
    void addMedicationTest() throws Exception {
        AddUpdateMedicationRequest medication = AddUpdateMedicationRequest.builder()
                .description("description")
                .unit(MILLIGRAMS)
                .dosage(BigDecimal.valueOf(20))
                .time(LocalTime.of(10, 30, 0))
                .build();
        mockMvc.perform(post("/api/patient/medication-add")
                        .contentType(APPLICATION_JSON)
                        .param("patientId", "7fe14f6e-5bd6-11ee-8c99-0242ac120002")
                        .content(objectMapper.writeValueAsString(List.of(medication))))
                .andExpect(status().isOk());
        assertEquals(3, medicationRepository.findAll().size());
    }

    @Test
    void addMedicationDescriptionValidationFailedTest() throws Exception {
        AddUpdateMedicationRequest medication = AddUpdateMedicationRequest.builder()
                .description("t")
                .unit(MILLIGRAMS)
                .dosage(BigDecimal.valueOf(20))
                .time(LocalTime.of(10, 30, 0))
                .build();
        mockMvc.perform(post("/api/patient/medication-add")
                        .contentType(APPLICATION_JSON)
                        .param("patientId", "7fe14f6e-5bd6-11ee-8c99-0242ac120002")
                        .content(objectMapper.writeValueAsString(List.of(medication))))
                .andExpect(status().isBadRequest());
        assertEquals(2, medicationRepository.findAll().size());
    }

    @Test
    void addMedicationDosageValidationFailedTest() throws Exception {
        AddUpdateMedicationRequest medication = AddUpdateMedicationRequest.builder()
                .description("description")
                .unit(MILLIGRAMS)
                .dosage(BigDecimal.valueOf(-2))
                .time(LocalTime.of(10, 30, 0))
                .build();
        mockMvc.perform(post("/api/patient/medication-add")
                        .contentType(APPLICATION_JSON)
                        .param("patientId", "7fe14f6e-5bd6-11ee-8c99-0242ac120002")
                        .content(objectMapper.writeValueAsString(List.of(medication))))
                .andExpect(status().isBadRequest());
        assertEquals(2, medicationRepository.findAll().size());
    }

    @Test
    void updateMedicationTest() throws Exception {
        AddUpdateMedicationRequest medication = AddUpdateMedicationRequest.builder()
                .description("update description")
                .unit(MILLIGRAMS)
                .dosage(BigDecimal.valueOf(20))
                .time(LocalTime.of(10, 30, 0))
                .build();
        mockMvc.perform(put("/api/patient/medication-update")
                        .contentType(APPLICATION_JSON)
                        .param("patientId", "7fe14f6e-5bd6-11ee-8c99-0242ac120002")
                        .param("medicationId", "7fe1528e-5bd6-11ee-8c99-0242ac120004")
                        .content(objectMapper.writeValueAsString(medication)))
                .andExpect(status().isOk());
        MedicationDbo medicationDbo = medicationRepository.findById(UUID.fromString("7fe1528e-5bd6-11ee-8c99-0242ac120004"))
                .orElseThrow(MedicationNotFoundException::new);
        assertEquals("update description", medicationDbo.getDescription());
    }

    @Test
    void getMedicationByPatientTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/patient/medication-by-patient")
                        .contentType(APPLICATION_JSON)
                        .param("patientId", "7fe14f6e-5bd6-11ee-8c99-0242ac120002"))
                .andExpect(status().isOk())
                .andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        List<MedicationDbo> medicationDbos =
                Arrays.asList(objectMapper.readValue(contentAsString, MedicationDbo[].class));
        assertEquals(2, medicationDbos.size());
    }

    @Test
    void findPatientById() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/patient/by-id")
                        .contentType(APPLICATION_JSON)
                        .param("id", "7fe14f6e-5bd6-11ee-8c99-0242ac120002"))
                .andExpect(status().isOk())
                .andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        PatientFullDetailsResponse patientFullDetailsResponse =
                objectMapper.readValue(contentAsString, PatientFullDetailsResponse.class);
        assertEquals(2, patientFullDetailsResponse.getMedications().size());
    }

    @Test
    void findAllPatientsTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/patient/patient-list")
                        .contentType(APPLICATION_JSON)
                        .param("size", "10")
                        .param("page", "0")
                        .param("criteria", "firstname"))
                .andExpect(status().isOk())
                .andReturn();
        String result = mvcResult.getResponse().getContentAsString();
        String response = """
                  {
                  "content": [
                    {
                      "lastname": "lastname",
                      "firstname": "firstname",
                      "gender": "Male",
                      "dateOfBirth": "1990-06-02",
                      "creationDate": "2020-06-02T11:12:00",
                      "modifyDate": "2021-06-02T11:12:00"
                    }
                  ],
                  "pageable": {
                    "pageNumber": 0,
                    "pageSize": 10,
                    "sort": {
                      "empty": false,
                      "sorted": true,
                      "unsorted": false
                    },
                    "offset": 0,
                    "paged": true,
                    "unpaged": false
                  },
                  "last": true,
                  "totalPages": 1,
                  "totalElements": 1,
                  "size": 10,
                  "number": 0,
                  "sort": {
                    "empty": false,
                    "sorted": true,
                    "unsorted": false
                  },
                  "first": true,
                  "numberOfElements": 1,
                  "empty": false
                }
                """;
        JSONAssert.assertEquals(response, result, JSONCompareMode.NON_EXTENSIBLE);
    }

}
