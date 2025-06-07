package com.cloudnrg.api.auditlog.infrastructure.persistence.jpa.repositories;

import com.cloudnrg.api.auditlog.domain.model.aggregates.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {
    List<AuditLog> findByUserId(UUID userId);
}