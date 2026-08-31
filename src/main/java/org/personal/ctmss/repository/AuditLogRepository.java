package org.personal.ctmss.repository;

import org.personal.ctmss.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {
    List<AuditLog> findByEntityNameAndEntityId(String entityName, UUID entityId);
}