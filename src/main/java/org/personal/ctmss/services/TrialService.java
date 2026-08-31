package org.personal.ctmss.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.personal.ctmss.dtos.TrialLifelineDTO;
import org.personal.ctmss.dtos.TrialUpdateRequest;
import org.personal.ctmss.entity.Status;
import org.personal.ctmss.entity.Trial;
import org.personal.ctmss.exceptions.ResourceNotFoundException;
import org.personal.ctmss.repository.TrialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TrialService {

    @Autowired
    private TrialRepository trialRepository;

    @Autowired
    private AuditService auditService;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public List<Trial> createTrail(List<Trial> trials){
        List<Trial> saved = trialRepository.saveAll(trials);
        for (Trial t : saved) {
            auditService.log("Trial", t.getId(), "CREATE", null, t);
        }
        return saved;
    }

    public Page<Trial> getTrails(Pageable pageable){
        return trialRepository.findAll(pageable);
    }

    public Trial getTrailById(UUID trialId){
        return trialRepository.findById(trialId).orElseThrow(()-> new ResourceNotFoundException("Trial not found with id " + trialId));
    }

    public Trial updateTrail(UUID id, TrialUpdateRequest request) {

        Trial existing = trialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trial not found with id " + id));

        Trial before = deepCopy(existing);

        if (request.getProtocol_no() != null) {
            existing.setProtocol_no(request.getProtocol_no());
        }

        if (request.getTrail_code() != null) {
            existing.setTrail_code(request.getTrail_code());
        }

        if (request.getTitle() != null) {
            existing.setTitle(request.getTitle());
        }

        if (request.getShort_title() != null) {
            existing.setShort_title(request.getShort_title());
        }

        if (request.getStudy_phase() != null) {
            existing.setStudy_phase(request.getStudy_phase());
        }

        if (request.getStudy_type() != null) {
            existing.setStudy_type(request.getStudy_type());
        }

        if (request.getStatus() != null) {
            existing.setStatus(request.getStatus());
        }

        if (request.getSponsor_team() != null) {
            existing.setSponsor_team(request.getSponsor_team());
        }

        if (request.getPrinciple_investigator() != null) {
            existing.setPrinciple_investigator(request.getPrinciple_investigator());
        }

        if (request.getIntervention_name() != null) {
            existing.setIntervention_name(request.getIntervention_name());
        }

        if (request.getIntervention_type() != null) {
            existing.setIntervention_type(request.getIntervention_type());
        }

        if (request.getTarget_patient() != null) {
            existing.setTarget_patient(request.getTarget_patient());
        }

        if (request.getStart_date() != null) {
            existing.setStart_date(request.getStart_date());
        }

        if (request.getExpected_end_date() != null) {
            existing.setExpected_end_date(request.getExpected_end_date());
        }

        if (request.getActual_end_date() != null) {
            existing.setActual_end_date(request.getActual_end_date());
        }

        if (request.getDescription() != null) {
            existing.setDescription(request.getDescription());
        }

        if (request.getPrimary_objective() != null) {
            existing.setPrimary_objective(request.getPrimary_objective());
        }

        if (request.getSecondary_objective() != null) {
            existing.setSecondary_objective(request.getSecondary_objective());
        }

        if (request.getIec_approval_date() != null) {
            existing.setIec_approval_date(request.getIec_approval_date());
        }

        if (request.getCtri_registration_number() != null) {
            existing.setCtri_registration_number(request.getCtri_registration_number());
        }

        if (request.getCtri_registration_date() != null) {
            existing.setCtri_registration_date(request.getCtri_registration_date());
        }

        if (request.getRegulatoryStage() != null) {
            existing.setRegulatoryStage(request.getRegulatoryStage());
        }

        Trial updated = trialRepository.save(existing);
        auditService.log("Trial", updated.getId(), "UPDATE", before, updated);
        return updated;
    }

    public void deleteTrail(UUID id) {

        Trial existing = trialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trail not found"));

        auditService.log("Trial", id, "DELETE", existing, null);
        trialRepository.deleteById(id);
    }

    public Page<Trial> findByStatus(Status status , Pageable pageable){

        return  trialRepository.findByStatus(status , pageable);
    }

    public TrialLifelineDTO getLifeline(UUID id) {
        Trial trial = getTrailById(id);
        return new TrialLifelineDTO(
                trial.getRegulatoryStage(),
                trial.getIec_approval_date(),
                trial.getCtri_registration_number(),
                trial.getCtri_registration_date()
        );
    }

    private Trial deepCopy(Trial trial) {
        try {
            return objectMapper.readValue(objectMapper.writeValueAsString(trial), Trial.class);
        } catch (Exception e) {
            return null; // fallback — audit log will just show null "before" if copy fails
        }
    }
}