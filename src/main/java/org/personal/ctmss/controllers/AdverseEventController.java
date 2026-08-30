package org.personal.ctmss.controllers;

import org.personal.ctmss.dtos.AdverseEventRequest;
import org.personal.ctmss.dtos.AdverseEventResponse;
import org.personal.ctmss.entity.CausalityStatus;
import org.personal.ctmss.services.AdverseEventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/adverse-events")
public class AdverseEventController {

    private final AdverseEventService adverseEventService;

    public AdverseEventController(AdverseEventService adverseEventService) {
        this.adverseEventService = adverseEventService;
    }

    @PostMapping
    public ResponseEntity<AdverseEventResponse> create(@RequestBody AdverseEventRequest request) {
        return ResponseEntity.ok(adverseEventService.createAdverseEvent(request));
    }

    @GetMapping("/by-patient/{patientId}")
    public ResponseEntity<List<AdverseEventResponse>> getByPatient(@PathVariable UUID patientId) {
        return ResponseEntity.ok(adverseEventService.getByPatient(patientId));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<AdverseEventResponse> updateStatus(@PathVariable UUID id, @RequestParam String status) {
        return ResponseEntity.ok(adverseEventService.updateStatus(id, status));
    }

    @PatchMapping("/{id}/causality-assessment")
    public ResponseEntity<AdverseEventResponse> assessCausality(@PathVariable UUID id, @RequestParam String status) {
        return ResponseEntity.ok(adverseEventService.assessCausality(id, CausalityStatus.valueOf(status)));
    }
}