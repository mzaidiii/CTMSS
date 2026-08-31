package org.personal.ctmss.controllers;

import jakarta.validation.Valid;
import org.personal.ctmss.entity.SiteStatus;
import org.personal.ctmss.entity.TrialSite;
import org.personal.ctmss.services.TrialSiteService;
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
@RequestMapping("/api/sites")
public class TrialSiteController {

    @Autowired
    TrialSiteService trialSiteService;

    @PostMapping
    public ResponseEntity<List<TrialSite>> createTrialSite (@Valid @RequestBody List<TrialSite> trialsite){
        List<TrialSite> result = trialSiteService.createTrialSite(trialsite);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping
    public Page<TrialSite> getAllTrialSite(@RequestParam(required = false) SiteStatus status , @PageableDefault(size = 10 , sort = "id")Pageable pageable){
        if (status == null){
            return trialSiteService.getallTrialSite(pageable);
        }
        return trialSiteService.findByStatus(status, pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrialSite> getTrialSiteById (@PathVariable UUID id ){
        TrialSite result = trialSiteService.getTrialSiteById(id);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrialSiteById(@PathVariable UUID id ){
        trialSiteService.deleteTrialSiteById(id);
        return ResponseEntity.noContent().build();
    }

}
