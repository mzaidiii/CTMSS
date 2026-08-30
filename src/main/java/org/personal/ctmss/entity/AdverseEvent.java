package org.personal.ctmss.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "adverse_events")
@Getter
@Setter
public class AdverseEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @NotNull
    private String description;

    @Enumerated(EnumType.STRING)
    @NotNull
    private AeSeverity severity;

    @NotNull
    private LocalDate reportedDate;

    @Enumerated(EnumType.STRING)
    private AeStatus status = AeStatus.OPEN;

    // --- Pharmacovigilance fields ---
    private String suspectedDrug;

    @Enumerated(EnumType.STRING)
    private PvCentreTier reportingTier;

    private String meddraCodeStub;

    private String whoDrugCodeStub;

    @Enumerated(EnumType.STRING)
    private CausalityStatus causalityStatus = CausalityStatus.PENDING_REVIEW;

    private LocalDate causalityAssessedDate;

    // --- Audit ---
    private String changedBy;

    @Column(updatable = false)
    private Instant createdAt;

    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }
}