package com.cloudnrg.api.auditlog.application.internal.queryservices;

import com.cloudnrg.api.auditlog.domain.model.aggregates.AuditLog;
import com.cloudnrg.api.auditlog.domain.model.queries.GetAuditLogsByFiltersQuery;
import com.cloudnrg.api.auditlog.domain.model.queries.GetAuditLogsByUserQuery;

import com.cloudnrg.api.auditlog.domain.services.AuditLogQueryService;

import com.cloudnrg.api.auditlog.infrastructure.persistence.jpa.repositories.AuditLogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditLogQueryServiceImpl implements AuditLogQueryService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogQueryServiceImpl(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Override
    public List<AuditLog> handle(GetAuditLogsByUserQuery query) {
        return auditLogRepository.findByUserId(query.userId());
    }

    //TODO: refactor into repository query method
    @Override
    public List<AuditLog> handle(GetAuditLogsByFiltersQuery query) {

        var all = auditLogRepository.findAll();
        return all.stream()
                .filter(log -> query.userId().map(id -> log.getUserId().equals(id)).orElse(true))
                .filter(log -> query.action().map(act -> log.getAction().equals(act)).orElse(true))
                .filter(log -> query.targetType().map(t -> log.getTarget().equals(t)).orElse(true))

                .toList();
    }
}