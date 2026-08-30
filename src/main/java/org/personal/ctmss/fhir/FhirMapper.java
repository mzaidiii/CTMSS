package org.personal.ctmss.fhir;

import org.hl7.fhir.r4.model.ResearchStudy;
import org.personal.ctmss.entity.RegulatoryStage;
import org.personal.ctmss.entity.Trial;
import org.springframework.stereotype.Component;

@Component
public class FhirMapper {

    public ResearchStudy toResearchStudy(Trial trial) {
        ResearchStudy study = new ResearchStudy();

        study.setId(trial.getId().toString());
        study.setTitle(trial.getTitle());

        if (trial.getCtri_registration_number() != null) {
            study.addIdentifier()
                    .setSystem("http://ctri.nic.in")
                    .setValue(trial.getCtri_registration_number());
        }

        study.setStatus(mapStatus(trial.getRegulatoryStage()));

        return study;
    }

    // RegulatoryStage doesn't map 1:1 to FHIR's ResearchStudyStatus - explicit translation needed
    private ResearchStudy.ResearchStudyStatus mapStatus(RegulatoryStage stage) {
        if (stage == null) return ResearchStudy.ResearchStudyStatus.NULL;
        return switch (stage) {
            case DRAFT -> ResearchStudy.ResearchStudyStatus.INREVIEW;
            case IEC_APPROVED, CTRI_REGISTERED -> ResearchStudy.ResearchStudyStatus.APPROVED;
            case ACTIVE -> ResearchStudy.ResearchStudyStatus.ACTIVE;
            case CLOSED_OUT -> ResearchStudy.ResearchStudyStatus.COMPLETED;
        };
    }
}