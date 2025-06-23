package com.cloudnrg.api.storage.application.internal.eventhandlers.folder;

import com.cloudnrg.api.shared.application.external.outboundedservices.ExternalAuditLogService;
import com.cloudnrg.api.storage.domain.model.events.UpdateFolderNameEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class FolderNameUpdateEventHandler {

    private final ExternalAuditLogService externalStorageAuditLogService;

    public FolderNameUpdateEventHandler(ExternalAuditLogService externalStorageAuditLogService) {
        this.externalStorageAuditLogService = externalStorageAuditLogService;
    }

    @EventListener(UpdateFolderNameEvent.class)
    public void on(UpdateFolderNameEvent event) {
        // Create audit log for the folder name update event
        externalStorageAuditLogService.createAuditLog(
                event.getUserId(),
                "UPDATE",
                "FOLDER",
                event.getFolderId().toString(),
                "Folder name updated to: " + event.getNewName()
        );
    }

}
