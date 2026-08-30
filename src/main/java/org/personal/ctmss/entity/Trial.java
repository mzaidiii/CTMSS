package org.personal.ctmss.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "trails")
public class Trial {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id ;
    @NotNull
    @Column(nullable = false,unique = true)
    private String protocol_no;
    @NotNull
    @Column(nullable = false,unique = true)
    private String trail_code;
    @NotNull
    private String title ;

    private String short_title;

    private String study_phase ;

    private String study_type;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;

    private String sponsor_team ;

    private String principle_investigator ;

    private String intervention_name;

    private String intervention_type;
    @Min(1)
    private int target_patient;

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

    @Enumerated(EnumType.STRING)
    @NotNull
    private RegulatoryStage regulatoryStage = RegulatoryStage.DRAFT;

    // --- Audit ---
    private String changedBy;

    @CreationTimestamp
    @Column(nullable = false,updatable = false)
    private Instant created_at;
    @UpdateTimestamp
    private Instant updated_at;
}