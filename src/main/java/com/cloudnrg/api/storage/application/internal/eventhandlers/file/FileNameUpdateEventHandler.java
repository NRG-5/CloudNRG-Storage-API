package com.cloudnrg.api.storage.application.internal.eventhandlers.file;

import com.cloudnrg.api.shared.application.external.outboundedservices.ExternalAuditLogService;
import com.cloudnrg.api.storage.domain.model.events.UpdateFileNameEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class FileNameUpdateEventHandler {

    private final ExternalAuditLogService externalAuditLogService;

    public FileNameUpdateEventHandler(ExternalAuditLogService externalAuditLogService) {
        this.externalAuditLogService = externalAuditLogService;
    }


    @EventListener(UpdateFileNameEvent.class)
    public void on(UpdateFileNameEvent event) {
        // Create object history for the file name update event


        externalAuditLogService.createAuditLog(
                event.getUserId(),
                "UPDATE",
                "FILE",
                event.getFileId().toString(),
                "File name updated from " + event.getOldFileName() + " to " + event.getNewFileName()
        );
    }

}
