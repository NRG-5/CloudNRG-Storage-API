package com.cloudnrg.api.storage.application.internal.eventhandlers.folder;

import com.cloudnrg.api.shared.application.external.outboundedservices.ExternalAuditLogService;
import com.cloudnrg.api.storage.domain.model.events.DeleteFolderEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class FolderDeletionEventHandler {

    private final ExternalAuditLogService externalStorageAuditLogService;

    public FolderDeletionEventHandler(ExternalAuditLogService externalStorageAuditLogService) {
        this.externalStorageAuditLogService = externalStorageAuditLogService;
    }

    @EventListener(DeleteFolderEvent.class)
    public void on(DeleteFolderEvent event) {
        // Create audit log for the folder deletion event
        externalStorageAuditLogService.createAuditLog(
                event.getUserId(),
                "DELETE",
                "FOLDER",
                event.getFolderId().toString(),
                "Folder deleted with ID: " + event.getFolderId()
        );
    }



}
