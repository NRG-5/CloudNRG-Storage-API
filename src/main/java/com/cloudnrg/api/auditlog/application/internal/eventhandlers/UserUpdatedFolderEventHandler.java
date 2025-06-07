package com.cloudnrg.api.auditlog.application.internal.eventhandlers;


import com.cloudnrg.api.auditlog.domain.model.aggregates.AuditLog;
import com.cloudnrg.api.auditlog.domain.model.events.UserUpdatedFolderEvent;
import com.cloudnrg.api.auditlog.domain.model.valueobjects.AuditAction;
import com.cloudnrg.api.auditlog.domain.model.valueobjects.AuditTargetType;
import com.cloudnrg.api.auditlog.infrastructure.persistence.jpa.repositories.AuditLogRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class UserUpdatedFolderEventHandler {

    private final AuditLogRepository auditLogRepository;

    public UserUpdatedFolderEventHandler(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @EventListener
    public void on(UserUpdatedFolderEvent event) {
        var log = new AuditLog(
                event.getUserId(),
                AuditAction.UPDATE,
                AuditTargetType.FOLDER,
                event.getFolderId().toString(),
                "User updated folder name to: " + event.getNewName()
        );
        auditLogRepository.save(log);
    }
}