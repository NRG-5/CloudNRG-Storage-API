package com.cloudnrg.api.auditlog.application.internal.eventhandlers;
import com.cloudnrg.api.auditlog.domain.model.aggregates.AuditLog;
import com.cloudnrg.api.auditlog.domain.model.events.UserDownloadedFileEvent;

import com.cloudnrg.api.auditlog.domain.model.valueobjects.AuditAction;
import com.cloudnrg.api.auditlog.domain.model.valueobjects.AuditTargetType;
import com.cloudnrg.api.auditlog.infrastructure.persistence.jpa.repositories.AuditLogRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
@Service
public class UserDownloadedFileEventHandler {

    private final AuditLogRepository auditLogRepository;

    public UserDownloadedFileEventHandler(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @EventListener
    public void on(UserDownloadedFileEvent event) {
        var log = new AuditLog(
                event.getUserId(),
                AuditAction.DOWNLOAD,
                AuditTargetType.FILE,
                event.getFileId().toString(),
                "User downloaded file: " + event.getFileName()
        );
        auditLogRepository.save(log);
    }

}
