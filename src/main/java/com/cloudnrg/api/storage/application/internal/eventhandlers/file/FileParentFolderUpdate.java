package com.cloudnrg.api.storage.application.internal.eventhandlers.file;

import com.cloudnrg.api.shared.application.external.outboundedservices.ExternalAuditLogService;
import com.cloudnrg.api.storage.domain.model.events.UpdateFileParentFolderEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class FileParentFolderUpdate {

    private final ExternalAuditLogService externalAuditLogService;

    public FileParentFolderUpdate(ExternalAuditLogService externalAuditLogService) {
        this.externalAuditLogService = externalAuditLogService;
    }


    @EventListener(UpdateFileParentFolderEvent.class)
    public void on(UpdateFileParentFolderEvent event) {
        // Create object history for the file parent folder update event


        externalAuditLogService.createAuditLog(
                event.getUserId(),
                "UPDATE",
                "FILE",
                event.getFileId().toString(),
                "File parent folder updated from " + event.getOldParentFolderName() + " to " + event.getNewParentFolderName()
        );
    }

}
