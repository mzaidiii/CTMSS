package org.personal.ctmss.services;

import org.personal.ctmss.entity.Visit;
import org.personal.ctmss.entity.VisitStatus;
import org.personal.ctmss.exceptions.ResourceNotFoundException;
import org.personal.ctmss.repository.VisitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class VisitService {

    @Autowired
    VisitRepository visitRepository;

    private static final int ALLOWED_WINDOW_DAYS = 3;

    public Visit createVisit(Visit visit) {
        return visitRepository.save(visit);
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

        return visitRepository.save(existing);
    }

    public void deleteVisitById(UUID id) {
        visitRepository.deleteById(id);
    }
}