package com.cloudnrg.api.auditlog.application.internal.eventhandlers;

import com.cloudnrg.api.auditlog.domain.model.aggregates.AuditLog;
import com.cloudnrg.api.auditlog.domain.model.events.UserDeletedEntityEvent;
import com.cloudnrg.api.auditlog.domain.model.valueobjects.AuditAction;
import com.cloudnrg.api.auditlog.infrastructure.persistence.jpa.repositories.AuditLogRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class UserDeletedEntityEventHandler {

    private final AuditLogRepository auditLogRepository;

    public UserDeletedEntityEventHandler(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @EventListener
    public void on(UserDeletedEntityEvent event) {
        var log = new AuditLog(
                event.getUserId(),
                AuditAction.DELETE,
                event.getTarget(),
                event.getTargetId().toString(),
                event.getDescription()
        );
        auditLogRepository.save(log);
    }
}