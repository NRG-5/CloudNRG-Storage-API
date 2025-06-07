package com.cloudnrg.api.auditlog.application.internal.eventhandlers;

import com.cloudnrg.api.auditlog.domain.model.aggregates.AuditLog;
import com.cloudnrg.api.auditlog.domain.model.events.UserUpdatedEntityEvent;
import com.cloudnrg.api.auditlog.domain.model.valueobjects.AuditAction;
import com.cloudnrg.api.auditlog.infrastructure.persistence.jpa.repositories.AuditLogRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class UserUpdatedEntityEventHandler {

    private final AuditLogRepository auditLogRepository;

    public UserUpdatedEntityEventHandler(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @EventListener
    public void on(UserUpdatedEntityEvent event) {
        var log = new AuditLog(
                event.getUserId(),
                AuditAction.UPDATE,
                event.getTarget(),
                event.getTargetId().toString(),
                event.getDescription()
        );
        auditLogRepository.save(log);
    }
}