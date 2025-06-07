package com.cloudnrg.api.auditlog.domain.model.queries;

import com.cloudnrg.api.auditlog.domain.model.valueobjects.AuditAction;
import com.cloudnrg.api.auditlog.domain.model.valueobjects.AuditTargetType;

import java.util.Optional;
import java.util.UUID;

public record GetAuditLogsByFiltersQuery(
        Optional<UUID> userId,
        Optional<AuditAction> action,
        Optional<AuditTargetType> targetType
) {}
