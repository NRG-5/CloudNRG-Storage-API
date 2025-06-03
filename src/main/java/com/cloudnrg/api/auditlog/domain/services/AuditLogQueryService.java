package com.cloudnrg.api.auditlog.domain.services;

import com.cloudnrg.api.auditlog.domain.model.aggregates.AuditLog;
import com.cloudnrg.api.auditlog.domain.model.queries.GetAuditLogsByUserQuery;

import java.util.List;

public interface AuditLogQueryService {
    List<AuditLog> handle(GetAuditLogsByUserQuery query);
}
