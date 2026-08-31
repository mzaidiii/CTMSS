package org.personal.ctmss.services;

import org.personal.ctmss.dtos.PatientAlertDTO;
import org.personal.ctmss.dtos.SiteKpiDTO;
import org.personal.ctmss.dtos.TrialKpiDTO;
import org.personal.ctmss.entity.*;
import org.personal.ctmss.exceptions.ResourceNotFoundException;
import org.personal.ctmss.repository.*;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class KpiService {

    private final TrialRepository trialRepository;
    private final PatientRepository patientRepository;
    private final VisitRepository visitRepository;
    private final TrialSiteRepository trialSiteRepository;
    private final AdverseEventRepository  adverseEventRepository;

    public  KpiService(TrialRepository trialRepository, PatientRepository patientRepository, VisitRepository visitRepository , TrialSiteRepository trialSiteRepository ,  AdverseEventRepository adverseEventRepository) {
        this.trialRepository = trialRepository;
        this.patientRepository = patientRepository;
        this.visitRepository = visitRepository;
        this.trialSiteRepository = trialSiteRepository;
        this.adverseEventRepository = adverseEventRepository;
    }

    public TrialKpiDTO getTrialKpi(UUID trialId) {
        Trial trial = trialRepository.findById(trialId)
                .orElseThrow(() -> new ResourceNotFoundException("Trial not found: " + trialId));

        long recruited = patientRepository.countByTrial_IdAndStatusNot(trialId, PatientStatus.WITHDRAWN);
        long deviations = visitRepository.countDeviationsByTrial(trialId);
        long overdue = visitRepository.countOverdueVisitsByTrial(trialId , VisitStatus.SCHEDULED);

        int target = trial.getTarget_patient();
        double enrollmentPercent = target > 0 ? (recruited * 100.0 / target) : 0.0;

        return new TrialKpiDTO(trial.getId(), trial.getTitle(), target,
                recruited, enrollmentPercent, deviations, overdue);
    }

    public SiteKpiDTO getSiteKpi(UUID siteId) {
        TrialSite site = trialSiteRepository.findById(siteId)
                .orElseThrow(() -> new ResourceNotFoundException("Site not found: " + siteId));

        int target = site.getTarget_patient();
        int recruited = site.getRecruited_patient();

        double enrollmentPercent = target > 0 ? (recruited * 100.0 / target) : 0.0;

        long deviations = visitRepository.countDeviationsBySite(siteId);
        long overdue = visitRepository.countOverdueVisitsBySite(siteId, VisitStatus.SCHEDULED);

        return new SiteKpiDTO(site.getId(), site.getSite_name(), target,
                recruited, enrollmentPercent, deviations, overdue);
    }


    private static final int MISSED_VISIT_THRESHOLD = 3;

    public List<PatientAlertDTO> getMissedVisitAlerts(UUID trialId ) {
        List<Patient> patients =patientRepository.findByTrial_Id(trialId);

        List<PatientAlertDTO> alerts = new ArrayList<>();

        for (Patient patient : patients) {
            long missedCount = visitRepository.countByPatient_IdAndStatus(patient.getId(), VisitStatus.MISSED);

            if (missedCount >= MISSED_VISIT_THRESHOLD) {
                String message = String.format(
                        "Patient %s (UHID: %s) at %s in %s has missed %d visits",
                        patient.getName(),
                        patient.getUhid(),
                        patient.getSite().getSite_name(),
                        patient.getTrial().getTitle(),
                        missedCount
                );

                alerts.add(new PatientAlertDTO(
                        patient.getId(),
                        patient.getName(),
                        patient.getUhid(),
                        patient.getSite().getSite_name(),
                        patient.getTrial().getTitle(),
                        "MISSED_VISITS",
                        "HIGH",
                        message
                ));
            }
        }

        return alerts;
    }

    public List<PatientAlertDTO> getAttentionAlerts(UUID trialId) {
        List<AdverseEvent> openAEs = adverseEventRepository.findByPatient_Trial_IdAndStatus(trialId , AeStatus.OPEN);

        List<PatientAlertDTO> alerts = new ArrayList<>();

        for (AdverseEvent ae : openAEs) {
            Patient patient = ae.getPatient();

            String message = String.format(
                    "Patient %s (UHID: %s) at %s in %s needs attention: %s (%s)",
                    patient.getName(),
                    patient.getUhid(),
                    patient.getSite().getSite_name(),
                    patient.getTrial().getTitle(),
                    ae.getDescription(),
                    ae.getSeverity().name()
            );

            alerts.add(new PatientAlertDTO(
                    patient.getId(),
                    patient.getName(),
                    patient.getUhid(),
                    patient.getSite().getSite_name(),
                    patient.getTrial().getTitle(),
                    "ADVERSE_EVENT",
                    ae.getSeverity() == AeSeverity.SAE ? "HIGH" : "MEDIUM",
                    message
            ));
        }

        return alerts;
    }
}
