package org.personal.ctmss.dtos;

import lombok.Getter;
import lombok.Setter;
import org.personal.ctmss.entity.RegulatoryStage;
import org.personal.ctmss.entity.Status;

import java.time.LocalDate;

@Getter
@Setter
public class TrialUpdateRequest {
    private String protocol_no;
    private String trail_code;
    private String title;
    private String short_title;
    private String study_phase;
    private String study_type;
    private Status status;
    private String sponsor_team;
    private String principle_investigator;
    private String intervention_name;
    private String intervention_type;
    private Integer target_patient;
    private LocalDate start_date;
    private LocalDate expected_end_date;
    private LocalDate actual_end_date;
    private String description;
    private String primary_objective;
    private String secondary_objective;

    // --- Regulatory lifeline fields ---
    private LocalDate iec_approval_date;
    private String ctri_registration_number;
    private LocalDate ctri_registration_date;
    private RegulatoryStage regulatoryStage;
}