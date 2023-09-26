package com.example.healthcareservice.controller;

import com.example.healthcareservice.model.entity.MedicationDbo;
import com.example.healthcareservice.model.request.AddUpdateMedicationRequest;
import com.example.healthcareservice.model.request.AddUpdatePatientRequest;
import com.example.healthcareservice.model.response.PatientFullDetailsResponse;
import com.example.healthcareservice.model.response.PatientGetResponse;
import com.example.healthcareservice.services.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("api/patient")
@RequiredArgsConstructor
public class PatientController {
    private final PatientService patientService;

    @GetMapping("/by-id")
    @ResponseStatus(OK)
    public PatientFullDetailsResponse findPatientById(@RequestParam("id") UUID patientId) {
        return patientService.findById(patientId);
    }

    @GetMapping("/patient-list")
    @ResponseStatus(OK)
    public Page<PatientGetResponse> getPatientList(@RequestParam("size") Integer size,
                                                   @RequestParam("page") Integer page,
                                                   @RequestParam("criteria") String criteria) {
        return patientService.findAllPatients(size, page, criteria);
    }

    @PostMapping("/create")
    @ResponseStatus(OK)
    public void addPatient(@Valid @RequestBody AddUpdatePatientRequest request) {
        patientService.createPatient(request);
    }

    @PutMapping("/update")
    @ResponseStatus(OK)
    public void updatePatient(@RequestParam("id") UUID patientId, @Valid @RequestBody AddUpdatePatientRequest request) {
        patientService.updatePatient(patientId, request);
    }

    @DeleteMapping("/delete")
    @ResponseStatus(OK)
    public void removePatient(@RequestParam("id") UUID patientId) {
        patientService.deletePatient(patientId);
    }

    @PostMapping("/medication-add")
    @ResponseStatus(OK)
    public void addMedicationByPatient(@RequestParam("patientId") UUID patientId,
                                       @RequestBody List<AddUpdateMedicationRequest> medicatiionList) {
        patientService.addMedicationsByPatient(patientId, medicatiionList);
    }

    @DeleteMapping("/medication-remove")
    @ResponseStatus(OK)
    public void removeMedicationByPatient(@RequestParam("patientId") UUID patientId,
                                          @RequestParam("medicationId") UUID medicationId) {
        patientService.removeMedication(patientId, medicationId);
    }

    @PutMapping("/medication-update")
    @ResponseStatus(OK)
    public void updateMedicationByPatient(@RequestParam("patientId") UUID patientId,
                                          @RequestParam("medicationId") UUID medicationId,
                                          @RequestBody AddUpdateMedicationRequest request) {
        patientService.updateMedicationByPatient(patientId, medicationId, request);
    }

    @GetMapping("/medication-by-patient")
    @ResponseStatus(OK)
    public List<MedicationDbo> findMedicationByPatient(@RequestParam("patientId") UUID patientId) {
        return patientService.getMedicationsByPatient(patientId);
    }
}
