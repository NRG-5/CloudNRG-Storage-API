package com.cloudnrg.api.storage.application.internal.eventhandlers.file;

import com.cloudnrg.api.shared.application.external.outboundedservices.ExternalAuditLogService;
import com.cloudnrg.api.storage.application.internal.outboundservices.acl.ExternalObjectHistoryService;
import com.cloudnrg.api.storage.domain.model.events.DeleteFileEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class FileDeletionEventHandler {
    private final ExternalObjectHistoryService externalObjectHistoryService;
    private final ExternalAuditLogService externalAuditLogService;

    public FileDeletionEventHandler(ExternalObjectHistoryService externalObjectHistoryService, ExternalAuditLogService externalAuditLogService) {
        this.externalObjectHistoryService = externalObjectHistoryService;
        this.externalAuditLogService = externalAuditLogService;
    }


    @EventListener(DeleteFileEvent.class)
    public void on(DeleteFileEvent event) {
        externalObjectHistoryService.deleteAllObjectsHistoryByFileId(event.getFileId());

        externalAuditLogService.createAuditLog(
                event.getUserId(),
                "DELETE",
                "FILE",
                event.getFileId().toString(),
                "File deleted with ID: " + event.getFileId()
        );

    }

}
