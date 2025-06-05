package com.cloudnrg.api.auditlog.interfaces.rest.resources;

import com.cloudnrg.api.auditlog.domain.model.valueobjects.AuditAction;
import com.cloudnrg.api.auditlog.domain.model.valueobjects.AuditTargetType;

import java.time.Instant;
import java.util.UUID;

public record AuditLogResource(
        UUID id,
        UUID userId,
        AuditAction action,
        AuditTargetType target,
        String targetId,
        String description,
        Instant createdAt,
        Instant updatedAt
) {}
