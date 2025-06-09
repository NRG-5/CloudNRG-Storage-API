package com.cloudnrg.api.auditlog.application.internal.eventhandlers;

import com.cloudnrg.api.auditlog.domain.model.aggregates.AuditLog;
import com.cloudnrg.api.auditlog.domain.model.events.UserDeletedFileEvent;
import com.cloudnrg.api.auditlog.domain.model.valueobjects.AuditAction;
import com.cloudnrg.api.auditlog.domain.model.valueobjects.AuditTargetType;
import com.cloudnrg.api.auditlog.infrastructure.persistence.jpa.repositories.AuditLogRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class UserDeletedFileEventHandler {

    private final AuditLogRepository auditLogRepository;

    public UserDeletedFileEventHandler(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @EventListener
    public void on(UserDeletedFileEvent event) {
        var log = new AuditLog(
                event.getUserId(),
                AuditAction.DELETE,
                AuditTargetType.FILE,
                event.getFileId().toString(),
                "User deleted file: " + event.getFileName()
        );
        auditLogRepository.save(log);
    }
}