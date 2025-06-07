package com.cloudnrg.api.auditlog.application.internal.eventhandlers;

import com.cloudnrg.api.auditlog.domain.model.aggregates.AuditLog;
import com.cloudnrg.api.auditlog.domain.model.valueobjects.AuditAction;
import com.cloudnrg.api.auditlog.domain.model.valueobjects.AuditTargetType;
import com.cloudnrg.api.auditlog.infrastructure.persistence.jpa.repositories.AuditLogRepository;
import com.cloudnrg.api.iam.domain.model.events.UserCreationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class UserAuditEventHandler {

    private final AuditLogRepository auditLogRepository;

    public UserAuditEventHandler(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @EventListener
    public void on(UserCreationEvent event) {
        var log = new AuditLog(
                event.getUserId(),
                AuditAction.CREATE,
                AuditTargetType.USER,
                event.getUserId().toString(),
                "User account created"
        );
        auditLogRepository.save(log);
    }
}
