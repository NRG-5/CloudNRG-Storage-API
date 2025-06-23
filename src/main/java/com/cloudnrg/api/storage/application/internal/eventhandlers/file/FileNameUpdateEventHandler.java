package com.cloudnrg.api.storage.application.internal.eventhandlers.file;

import com.cloudnrg.api.shared.application.external.outboundedservices.ExternalAuditLogService;
import com.cloudnrg.api.storage.application.internal.outboundservices.acl.ExternalObjectHistoryService;
import com.cloudnrg.api.storage.domain.model.events.UpdateFileNameEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class FileNameUpdateEventHandler {
    private final ExternalObjectHistoryService externalObjectHistoryService;
    private final ExternalAuditLogService externalAuditLogService;

    public FileNameUpdateEventHandler(ExternalObjectHistoryService externalObjectHistoryService, ExternalAuditLogService externalAuditLogService) {
        this.externalObjectHistoryService = externalObjectHistoryService;
        this.externalAuditLogService = externalAuditLogService;
    }


    @EventListener(UpdateFileNameEvent.class)
    public void on(UpdateFileNameEvent event) {
        externalObjectHistoryService.saveObjectHistoryUpdateFileName(event.getFileId(), event.getUserId(), event.getNewFileName());

        externalAuditLogService.createAuditLog(
                event.getUserId(),
                "UPDATE",
                "FILE",
                event.getFileId().toString(),
                "File name updated from " + event.getOldFileName() + " to " + event.getNewFileName()
        );
    }

}
