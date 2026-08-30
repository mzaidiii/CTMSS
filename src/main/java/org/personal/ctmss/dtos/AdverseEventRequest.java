package org.personal.ctmss.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class AdverseEventRequest {
    private UUID patientId;
    private String description;
    private String severity;
    private LocalDate reportedDate;

    // --- Pharmacovigilance fields (causalityStatus deliberately excluded - set later, not at creation) ---
    private String suspectedDrug;
    private String reportingTier;
    private String meddraCodeStub;
    private String whoDrugCodeStub;
}