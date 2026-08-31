package org.personal.ctmss.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.personal.ctmss.entity.Visit;
import org.personal.ctmss.entity.VisitStatus;
import org.personal.ctmss.exceptions.ResourceNotFoundException;
import org.personal.ctmss.repository.VisitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class VisitService {

    @Autowired
    VisitRepository visitRepository;

    @Autowired
    AuditService auditService;

    private static final int ALLOWED_WINDOW_DAYS = 3;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public Visit createVisit(Visit visit) {
        Visit saved = visitRepository.save(visit);
        auditService.log("Visit", saved.getId(), "CREATE", null, saved);
        return saved;
    }

    public Page<Visit> getVisitsByPatient(UUID patientId, Pageable pageable) {
        return visitRepository.findByPatient_Id(patientId, pageable);
    }

    public Visit getVisitById(UUID id) {
        return visitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No Visit Found with id :- " + id));
    }

    public Visit updateVisit(UUID id, Visit updated) {
        Visit existing = getVisitById(id);
        Visit before = deepCopy(existing);

        if (updated.getStatus() != null) {
            existing.setStatus(updated.getStatus());
        }
        if (updated.getActualDate() != null) {
            existing.setActualDate(updated.getActualDate());
        }
        if (updated.getNotes() != null) {
            existing.setNotes(updated.getNotes());
        }
        if (updated.getProtocolDeviation() != null) {
            existing.setProtocolDeviation(updated.getProtocolDeviation());
        }

        if (existing.getScheduledDate() != null && existing.getActualDate() != null && existing.getStatus() == VisitStatus.COMPLETED) {
            long diff = Math.abs(ChronoUnit.DAYS.between(existing.getScheduledDate(), existing.getActualDate()));
            existing.setProtocolDeviation(diff > ALLOWED_WINDOW_DAYS);
        }

        Visit saved = visitRepository.save(existing);
        auditService.log("Visit", saved.getId(), "UPDATE", before, saved);
        return saved;
    }

    public void deleteVisitById(UUID id) {
        Visit existing = getVisitById(id);
        auditService.log("Visit", id, "DELETE", existing, null);
        visitRepository.deleteById(id);
    }

    private Visit deepCopy(Visit visit) {
        try {
            return objectMapper.readValue(objectMapper.writeValueAsString(visit), Visit.class);
        } catch (Exception e) {
            return null;
        }
    }
}