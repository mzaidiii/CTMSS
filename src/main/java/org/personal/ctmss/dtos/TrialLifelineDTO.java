package org.personal.ctmss.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.personal.ctmss.entity.RegulatoryStage;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class TrialLifelineDTO {
    private RegulatoryStage regulatoryStage;
    private LocalDate iec_approval_date;
    private String ctri_registration_number;
    private LocalDate ctri_registration_date;
}