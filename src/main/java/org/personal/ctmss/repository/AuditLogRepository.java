package org.personal.ctmss.repository;

import org.personal.ctmss.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {
    List<AuditLog> findByEntityNameAndEntityId(String entityName, UUID entityId);

    @Query("SELECT DISTINCT a.entityId FROM AuditLog a WHERE a.entityName = :entityName AND a.action = :action")
    List<UUID> findDistinctEntityIdsByEntityNameAndAction(String entityName, String action);
}