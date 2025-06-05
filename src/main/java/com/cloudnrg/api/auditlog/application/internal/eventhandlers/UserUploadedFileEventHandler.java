package com.cloudnrg.api.auditlog.application.internal.eventhandlers;
import com.cloudnrg.api.auditlog.domain.model.aggregates.AuditLog;

import com.cloudnrg.api.auditlog.domain.model.events.UserUploadedFileEvent;
import com.cloudnrg.api.auditlog.domain.model.valueobjects.AuditAction;
import com.cloudnrg.api.auditlog.domain.model.valueobjects.AuditTargetType;
import com.cloudnrg.api.auditlog.infrastructure.persistence.jpa.repositories.AuditLogRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
@Service
public class UserUploadedFileEventHandler {

    private final AuditLogRepository auditLogRepository;

    public UserUploadedFileEventHandler(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @EventListener
    public void on(UserUploadedFileEvent event) {
        var log = new AuditLog(
                event.getUserId(),
                AuditAction.CREATE,
                AuditTargetType.FILE,
                event.getFileId().toString(),
                "User uploaded file: " + event.getFileName()
        );
        auditLogRepository.save(log);
    }

}
