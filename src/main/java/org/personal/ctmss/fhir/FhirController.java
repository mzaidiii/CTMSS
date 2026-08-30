package org.personal.ctmss.fhir;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.r4.model.ResearchStudy;
import org.personal.ctmss.entity.Trial;
import org.personal.ctmss.services.TrialService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/fhir")
public class FhirController {

    private final TrialService trialService;
    private final FhirMapper fhirMapper;
    private final FhirContext fhirContext = FhirContext.forR4();

    public FhirController(TrialService trialService, FhirMapper fhirMapper) {
        this.trialService = trialService;
        this.fhirMapper = fhirMapper;
    }

    @GetMapping(value = "/ResearchStudy/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getResearchStudy(@PathVariable UUID id) {
        Trial trial = trialService.getTrailById(id);
        ResearchStudy study = fhirMapper.toResearchStudy(trial);
        return fhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(study);
    }
}