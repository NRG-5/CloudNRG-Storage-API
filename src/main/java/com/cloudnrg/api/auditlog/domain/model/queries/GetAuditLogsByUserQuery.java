package com.cloudnrg.api.auditlog.domain.model.queries;

import java.util.UUID;

public record GetAuditLogsByUserQuery(UUID userId) {}