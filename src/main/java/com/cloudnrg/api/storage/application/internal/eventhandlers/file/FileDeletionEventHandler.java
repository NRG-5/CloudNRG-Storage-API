package com.cloudnrg.api.storage.application.internal.eventhandlers.file;

import com.cloudnrg.api.shared.application.external.outboundedservices.ExternalAuditLogService;
import com.cloudnrg.api.storage.domain.model.events.DeleteFileEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class FileDeletionEventHandler {

    private final ExternalAuditLogService externalAuditLogService;

    public FileDeletionEventHandler(ExternalAuditLogService externalAuditLogService) {
        this.externalAuditLogService = externalAuditLogService;
    }


    @EventListener(DeleteFileEvent.class)
    public void on(DeleteFileEvent event) {
        //delete object history for the file deletion event
        //externalObjectHistoryService.deleteObjectHistoriesByFileId(event.getFileId());

        externalAuditLogService.createAuditLog(
                event.getUserId(),
                "DELETE",
                "FILE",
                event.getFileId().toString(),
                "File deleted with ID: " + event.getFileId()
        );

    }

}
