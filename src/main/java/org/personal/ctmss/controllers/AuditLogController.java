package org.personal.ctmss.controllers;

import org.personal.ctmss.entity.AuditLog;
import org.personal.ctmss.entity.Trial;
import org.personal.ctmss.repository.AuditLogRepository;
import org.personal.ctmss.repository.TrialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/audit-logs")
public class AuditLogController {

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Autowired
    private TrialRepository trialRepository;

    @GetMapping("/{entityName}/{entityId}")
    public List<AuditLog> getLogsForEntity(@PathVariable String entityName, @PathVariable UUID entityId) {
        return auditLogRepository.findByEntityNameAndEntityId(entityName, entityId);
    }

    @GetMapping
    public List<AuditLog> getAllLogs() {
        return auditLogRepository.findAll();
    }

    @GetMapping("/trials/updated")
    public List<Trial> getUpdatedTrials() {
        List<UUID> updatedTrialIds = auditLogRepository.findDistinctEntityIdsByEntityNameAndAction("Trial", "UPDATE");
        return trialRepository.findAllById(updatedTrialIds);
    }
}