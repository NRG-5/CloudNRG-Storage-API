package com.cloudnrg.api.storage.application.internal.eventhandlers.folder;

import com.cloudnrg.api.shared.application.external.outboundedservices.ExternalAuditLogService;
import com.cloudnrg.api.storage.domain.model.events.CreateFolderEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class FolderCreationEventHandler {

    private final ExternalAuditLogService externalAuditLogService;

    public FolderCreationEventHandler(ExternalAuditLogService externalAuditLogService) {
        this.externalAuditLogService = externalAuditLogService;
    }

    @EventListener(CreateFolderEvent.class)
    private void on(CreateFolderEvent event) {
        // Create audit log for the folder creation event
        externalAuditLogService.createAuditLog(
                event.getUserId(),
                "CREATE",
                "FOLDER",
                event.getFolderId().toString(),
                "Folder created with ID: " + event.getFolderId()
        );
    }

}
