package org.personal.ctmss.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.personal.ctmss.dtos.AdverseEventRequest;
import org.personal.ctmss.dtos.AdverseEventResponse;
import org.personal.ctmss.entity.AdverseEvent;
import org.personal.ctmss.entity.AeSeverity;
import org.personal.ctmss.entity.AeStatus;
import org.personal.ctmss.entity.CausalityStatus;
import org.personal.ctmss.entity.Patient;
import org.personal.ctmss.entity.PvCentreTier;
import org.personal.ctmss.exceptions.ResourceNotFoundException;
import org.personal.ctmss.repository.AdverseEventRepository;
import org.personal.ctmss.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AdverseEventService {

    private final AdverseEventRepository adverseEventRepository;
    private final PatientRepository patientRepository;
    private final AuditService auditService;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public AdverseEventService(AdverseEventRepository adverseEventRepository,
                               PatientRepository patientRepository,
                               AuditService auditService) {
        this.adverseEventRepository = adverseEventRepository;
        this.patientRepository = patientRepository;
        this.auditService = auditService;
    }

    public AdverseEventResponse createAdverseEvent(AdverseEventRequest request) {
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found: " + request.getPatientId()));

        AdverseEvent ae = new AdverseEvent();
        ae.setPatient(patient);
        ae.setDescription(request.getDescription());
        ae.setSeverity(AeSeverity.valueOf(request.getSeverity()));
        ae.setReportedDate(request.getReportedDate());
        ae.setSuspectedDrug(request.getSuspectedDrug());
        if (request.getReportingTier() != null) {
            ae.setReportingTier(PvCentreTier.valueOf(request.getReportingTier()));
        }
        ae.setMeddraCodeStub(request.getMeddraCodeStub());
        ae.setWhoDrugCodeStub(request.getWhoDrugCodeStub());

        AdverseEvent saved = adverseEventRepository.save(ae);
        auditService.log("AdverseEvent", saved.getId(), "CREATE", null, saved);
        return toResponse(saved);
    }

    public List<AdverseEventResponse> getByPatient(UUID patientId) {
        return adverseEventRepository.findByPatient_Id(patientId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public AdverseEventResponse updateStatus(UUID aeId, String newStatus) {
        AdverseEvent ae = adverseEventRepository.findById(aeId)
                .orElseThrow(() -> new ResourceNotFoundException("Adverse event not found: " + aeId));

        AdverseEvent before = deepCopy(ae);
        ae.setStatus(AeStatus.valueOf(newStatus));
        AdverseEvent saved = adverseEventRepository.save(ae);
        auditService.log("AdverseEvent", saved.getId(), "UPDATE", before, saved);
        return toResponse(saved);
    }

    public AdverseEventResponse assessCausality(UUID aeId, CausalityStatus status) {
        AdverseEvent ae = adverseEventRepository.findById(aeId)
                .orElseThrow(() -> new ResourceNotFoundException("Adverse event not found: " + aeId));

        AdverseEvent before = deepCopy(ae);
        ae.setCausalityStatus(status);
        ae.setCausalityAssessedDate(LocalDate.now());
        AdverseEvent saved = adverseEventRepository.save(ae);
        auditService.log("AdverseEvent", saved.getId(), "UPDATE", before, saved);
        return toResponse(saved);
    }

    private AdverseEventResponse toResponse(AdverseEvent ae) {
        return new AdverseEventResponse(
                ae.getId(),
                ae.getPatient().getId(),
                ae.getPatient().getName(),
                ae.getDescription(),
                ae.getSeverity().name(),
                ae.getStatus().name(),
                ae.getReportedDate(),
                ae.getSuspectedDrug(),
                ae.getReportingTier() != null ? ae.getReportingTier().name() : null,
                ae.getMeddraCodeStub(),
                ae.getWhoDrugCodeStub(),
                ae.getCausalityStatus() != null ? ae.getCausalityStatus().name() : null,
                ae.getCausalityAssessedDate()
        );
    }

    private AdverseEvent deepCopy(AdverseEvent ae) {
        try {
            return objectMapper.readValue(objectMapper.writeValueAsString(ae), AdverseEvent.class);
        } catch (Exception e) {
            return null;
        }
    }
}