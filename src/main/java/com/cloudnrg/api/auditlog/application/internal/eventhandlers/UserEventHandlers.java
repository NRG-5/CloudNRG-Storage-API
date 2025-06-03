package com.cloudnrg.api.auditlog.application.internal.eventhandlers;

import com.cloudnrg.api.auditlog.domain.model.aggregates.AuditLog;
import com.cloudnrg.api.auditlog.domain.model.events.UserDownloadedFileEvent;
import com.cloudnrg.api.auditlog.domain.model.events.UserUploadedFileEvent;
import com.cloudnrg.api.auditlog.domain.model.events.UserLoginEvent;
import com.cloudnrg.api.auditlog.domain.model.valueobjects.AuditAction;

import com.cloudnrg.api.auditlog.infrastructure.persistence.jpa.repositories.AuditLogRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class UserEventHandlers {

    private final AuditLogRepository auditLogRepository;

    public UserEventHandlers(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @EventListener
    public void on(UserLoginEvent event) {
        var log = new AuditLog(
                event.getUserId(),
                AuditAction.LOGIN,
                "User",
                event.getUserId().toString(),
                "User logged in successfully"
        );
        auditLogRepository.save(log);
    }

    @EventListener
    public void on(UserUploadedFileEvent event) {
        var log = new AuditLog(
                event.getUserId(),
                AuditAction.CREATE,
                "File",
                event.getFileId().toString(),
                "User uploaded file: " + event.getFileName()
        );
        auditLogRepository.save(log);
    }

    @EventListener
    public void on(UserDownloadedFileEvent event) {
        var log = new AuditLog(
                event.getUserId(),
                AuditAction.DOWNLOAD,
                "File",
                event.getFileId().toString(),
                "User downloaded file: " + event.getFileName()
        );
        auditLogRepository.save(log);
    }
}

