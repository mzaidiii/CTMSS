package org.personal.ctmss.controllers;


import jakarta.validation.Valid;
import org.personal.ctmss.dtos.TrialLifelineDTO;
import org.personal.ctmss.dtos.TrialUpdateRequest;
import org.personal.ctmss.entity.Status;
import org.personal.ctmss.entity.Trial;
import org.personal.ctmss.services.TrialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/trails")
public class TrialController {

    @Autowired
    private TrialService trialService;

    @PostMapping
    public ResponseEntity<List<Trial>> createTrail(@Valid @RequestBody List<Trial> trial){
        List<Trial> saved = trialService.createTrail(trial);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public Page<Trial> getAllTrails(@RequestParam(required = false) Status status , @PageableDefault(size = 10 , sort = "id")Pageable pageable ){
        if (status == null){
            return trialService.getTrails(pageable);
        }
        return trialService.findByStatus(status,pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Trial> getTrialById(@PathVariable UUID id){
        Trial trial = trialService.getTrailById(id);
        return ResponseEntity.ok(trial);
    }

    @GetMapping("/{id}/lifeline")
    public ResponseEntity<TrialLifelineDTO> getLifeline(@PathVariable UUID id){
        return ResponseEntity.ok(trialService.getLifeline(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Trial> updateTrial(@PathVariable UUID id , @Valid @RequestBody TrialUpdateRequest update){
        Trial request = trialService.updateTrail(id , update);
        return ResponseEntity.ok(request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrail(@PathVariable UUID id) {
        trialService.deleteTrail(id);
        return ResponseEntity.noContent().build();
    }

}