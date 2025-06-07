package com.cloudnrg.api.auditlog.application.internal.eventhandlers;


import com.cloudnrg.api.auditlog.domain.model.aggregates.AuditLog;
import com.cloudnrg.api.auditlog.domain.model.events.UserDeletedFolderEvent;
import com.cloudnrg.api.auditlog.domain.model.valueobjects.AuditAction;
import com.cloudnrg.api.auditlog.domain.model.valueobjects.AuditTargetType;
import com.cloudnrg.api.auditlog.infrastructure.persistence.jpa.repositories.AuditLogRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class UserDeletedFolderEventHandler {

    private final AuditLogRepository auditLogRepository;

    public UserDeletedFolderEventHandler(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @EventListener
    public void on(UserDeletedFolderEvent event) {
        var log = new AuditLog(
                event.getUserId(),
                AuditAction.DELETE,
                AuditTargetType.FOLDER,
                event.getFolderId().toString(),
                "User deleted folder: " + event.getFolderName()
        );
        auditLogRepository.save(log);
    }
}
