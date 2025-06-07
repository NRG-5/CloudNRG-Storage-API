package com.cloudnrg.api.auditlog.domain.model.commands;

import com.cloudnrg.api.auditlog.domain.model.valueobjects.AuditAction;
import com.cloudnrg.api.auditlog.domain.model.valueobjects.AuditTargetType;

import java.util.UUID;

public record RegisterAuditLogCommand(
        UUID userId,
        AuditAction action,
        AuditTargetType target,
        String targetId,
        String description
) {}