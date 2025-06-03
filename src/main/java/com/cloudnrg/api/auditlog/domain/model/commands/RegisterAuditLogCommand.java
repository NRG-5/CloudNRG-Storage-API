package com.cloudnrg.api.auditlog.domain.model.commands;

import com.cloudnrg.api.auditlog.domain.model.valueobjects.AuditAction;

import java.util.UUID;

public record RegisterAuditLogCommand(
        UUID userId,
        AuditAction action,
        String target,
        String targetId,
        String description
) {}