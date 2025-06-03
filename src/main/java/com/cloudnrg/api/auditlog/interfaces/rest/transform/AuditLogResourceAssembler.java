package com.cloudnrg.api.auditlog.interfaces.rest.transform;

import com.cloudnrg.api.auditlog.domain.model.aggregates.AuditLog;
import com.cloudnrg.api.auditlog.interfaces.rest.resources.AuditLogResource;


public class AuditLogResourceAssembler {
    public static AuditLogResource toResourceFromEntity(AuditLog log) {
        return new AuditLogResource(
                log.getId(),
                log.getUserId(),
                log.getAction(),
                log.getTarget(),
                log.getTargetId(),
                log.getDescription(),
                log.getCreatedAt(),
                log.getUpdatedAt()
        );
    }
}