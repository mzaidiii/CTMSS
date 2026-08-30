package org.personal.ctmss.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class AdverseEventResponse {
    private UUID id;
    private UUID patientId;
    private String patientName;
    private String description;
    private String severity;
    private String status;
    private LocalDate reportedDate;
    private String suspectedDrug;
    private String reportingTier;
    private String meddraCodeStub;
    private String whoDrugCodeStub;
    private String causalityStatus;
    private LocalDate causalityAssessedDate;
}