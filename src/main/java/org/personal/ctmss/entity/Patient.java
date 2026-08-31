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
@Table(name = "patient")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trial_id", nullable = false)
    private Trial trial;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id", nullable = false)
    private TrialSite site;

    @NotNull
    private String name;

    @NotNull
    @Column(nullable = false, unique = true)
    private String uhid; // hospital/govt ID hash — enforces 1 patient = 1 trial ever

    @NotNull
    @Column(nullable = false, unique = true)
    private String patient_code; // trial-specific screening ID

    @Min(0)
    private Integer age;
    private String gender;
    private String randomization_arm;

    @NotNull
    private LocalDate enrollment_date;

    @NotNull @Enumerated(EnumType.STRING)
    private PatientStatus status;

    @NotNull @Enumerated(EnumType.STRING)
    @Column(name = "consent_status")
    private ConsentStatus consentStatus;

    @Column(name = "consent_date")
    private LocalDate consentDate;
    @Column(name="withdrawal_reason")
    private String withdrawalReason;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant created_at;
    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updated_at;
}
