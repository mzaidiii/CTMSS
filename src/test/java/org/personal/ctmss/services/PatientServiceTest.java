package org.personal.ctmss.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.personal.ctmss.entity.Patient;
import org.personal.ctmss.entity.TrialSite;
import org.personal.ctmss.repository.PatientRepository;
import org.personal.ctmss.repository.TrialSiteRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private TrialSiteRepository trialSiteRepository;

    @Mock
    private AuditService auditService;

    @InjectMocks
    private PatientService patientService;

    @Test
    void shouldRejectPatientWhenSiteIsMissing() {
        Patient patient = new Patient();
        assertThrows(
                IllegalArgumentException.class,
                () -> patientService.createPatient(patient)
        );

        verifyNoInteractions(
                patientRepository,
                trialSiteRepository,
                auditService
        );
    }

    @Test
    void shouldRejectPatientWhenTrialIsMissing() {
        Patient patient = new Patient();

        TrialSite site = new TrialSite();
        site.setId(java.util.UUID.randomUUID());
        patient.setSite(site);

        assertThrows(
                IllegalArgumentException.class,
                () -> patientService.createPatient(patient)
        );

        verifyNoInteractions(
                patientRepository,
                trialSiteRepository,
                auditService
        );
    }
}