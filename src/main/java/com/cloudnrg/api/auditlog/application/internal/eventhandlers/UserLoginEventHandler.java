package com.cloudnrg.api.auditlog.application.internal.eventhandlers;

import com.cloudnrg.api.auditlog.domain.model.aggregates.AuditLog;
import com.cloudnrg.api.auditlog.domain.model.events.UserLoginEvent;
import com.cloudnrg.api.auditlog.domain.model.valueobjects.AuditAction;
import com.cloudnrg.api.auditlog.domain.model.valueobjects.AuditTargetType;
import com.cloudnrg.api.auditlog.infrastructure.persistence.jpa.repositories.AuditLogRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class UserLoginEventHandler {

    private final AuditLogRepository auditLogRepository;

    public UserLoginEventHandler(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @EventListener
    public void on(UserLoginEvent event) {
        var log = new AuditLog(
                event.getUserId(),
                AuditAction.LOGIN,
                AuditTargetType.USER,
                event.getUserId().toString(),
                "User logged in successfully"
        );
        auditLogRepository.save(log);
    }
}
