package org.personal.ctmss.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.personal.ctmss.entity.Patient;
import org.personal.ctmss.entity.TrialSite;
import org.personal.ctmss.exceptions.ResourceNotFoundException;
import org.personal.ctmss.repository.PatientRepository;
import org.personal.ctmss.repository.TrialSiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PatientService {

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    TrialSiteRepository trialSiteRepository;

    @Autowired
    AuditService auditService;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public Patient createPatient(Patient patient) {
        if (patient.getSite() == null || patient.getSite().getId() == null) {
            throw new IllegalArgumentException("Patient must be associated with a valid site ID");
        }
        if (patient.getTrial() == null || patient.getTrial().getId() == null) {
            throw new IllegalArgumentException("Patient must be associated with a valid trial ID");
        }

        UUID siteId = patient.getSite().getId();
        TrialSite site = trialSiteRepository.findById(siteId)
                .orElseThrow(() -> new ResourceNotFoundException("No Site Found with id :- " + siteId));

        if (!site.getTrial().getId().equals(patient.getTrial().getId())) {
            throw new IllegalArgumentException("Site does not belong to the specified trial");
        }

        Patient saved = patientRepository.save(patient);
        auditService.log("Patient", saved.getId(), "CREATE", null, saved);
        return saved;
    }

    public Page<Patient> getAllPatients(Pageable pageable) {
        return patientRepository.findAll(pageable);
    }

    public Patient getPatientById(UUID id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No Patient Found with id :- " + id));
    }

    public Page<Patient> getPatientsByTrial(UUID trialId, Pageable pageable) {
        return patientRepository.findByTrial_Id(trialId, pageable);
    }

    public Page<Patient> getPatientsBySite(UUID siteId, Pageable pageable) {
        return patientRepository.findBySite_Id(siteId, pageable);
    }

    public Patient updatePatient(UUID id, Patient updated) {
        Patient existing = getPatientById(id);
        Patient before = deepCopy(existing);

        if (updated.getStatus() != null) {
            existing.setStatus(updated.getStatus());
        }
        if (updated.getConsentStatus() != null) {
            existing.setConsentStatus(updated.getConsentStatus());
        }
        if (updated.getConsentDate() != null) {
            existing.setConsentDate(updated.getConsentDate());
        }
        if (updated.getWithdrawalReason() != null) {
            existing.setWithdrawalReason(updated.getWithdrawalReason());
        }

        Patient saved = patientRepository.save(existing);
        auditService.log("Patient", saved.getId(), "UPDATE", before, saved);
        return saved;
    }

    public void deletePatientById(UUID id) {
        Patient existing = getPatientById(id);
        auditService.log("Patient", id, "DELETE", existing, null);
        patientRepository.deleteById(id);
    }

    private Patient deepCopy(Patient patient) {
        try {
            return objectMapper.readValue(objectMapper.writeValueAsString(patient), Patient.class);
        } catch (Exception e) {
            return null;
        }
    }
}