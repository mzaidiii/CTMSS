package org.personal.ctmss.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.personal.ctmss.entity.AuditLog;
import org.personal.ctmss.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class AuditService {

    @Autowired
    private AuditLogRepository auditLogRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void log(String entityName, UUID entityId, String action, Object oldValue, Object newValue) {
        AuditLog log = new AuditLog();
        log.setEntityName(entityName);
        log.setEntityId(entityId);
        log.setAction(action);
        log.setChangedBy(getCurrentUsername());
        log.setOldValue(toJson(oldValue));
        log.setNewValue(toJson(newValue));
        auditLogRepository.save(log);
    }

    private String getCurrentUsername() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return (auth != null && auth.getName() != null) ? auth.getName() : "SYSTEM";
    }

    private String toJson(Object obj) {
        if (obj == null) return null;
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            return obj.toString();
        }
    }
}