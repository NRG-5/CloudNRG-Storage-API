package com.cloudnrg.api.storage.application.internal.eventhandlers.file;

import com.cloudnrg.api.shared.application.external.outboundedservices.ExternalAuditLogService;
import com.cloudnrg.api.storage.application.internal.outboundservices.acl.ExternalObjectHistoryService;
import com.cloudnrg.api.storage.domain.model.events.CreateFileEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class FileCreationEventHandler {
    private final ExternalObjectHistoryService externalObjectHistoryService;
    private final ExternalAuditLogService externalAuditLogService;

    public FileCreationEventHandler(ExternalObjectHistoryService externalObjectHistoryService, ExternalAuditLogService externalAuditLogService) {
        this.externalObjectHistoryService = externalObjectHistoryService;
        this.externalAuditLogService = externalAuditLogService;
    }

    @EventListener(CreateFileEvent.class)
    public void on(CreateFileEvent event) {
        externalObjectHistoryService.saveObjectHistoryCreateFile(event.getFileId(), event.getUserId());

        externalAuditLogService.createAuditLog(
                event.getUserId(),
                "CREATE",
                "FILE",
                event.getFileId().toString(),
                "File created with ID: " + event.getFileId()
        );
    }

}
