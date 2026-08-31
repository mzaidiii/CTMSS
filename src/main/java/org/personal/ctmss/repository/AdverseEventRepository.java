package org.personal.ctmss.repository;

import org.personal.ctmss.entity.AdverseEvent;
import org.personal.ctmss.entity.AeStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AdverseEventRepository extends JpaRepository<AdverseEvent, UUID> {
    List<AdverseEvent> findByPatient_Id(UUID patientId);

    List<AdverseEvent> findByPatient_Trial_IdAndStatus(UUID trialId, AeStatus status);
}
