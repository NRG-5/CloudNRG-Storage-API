package com.cloudnrg.api.storage.application.internal.eventhandlers.file;

import com.cloudnrg.api.storage.application.internal.outboundservices.acl.ExternalStorageAuditLogService;
import com.cloudnrg.api.storage.domain.model.events.UpdateFileParentFolderEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class FileParentFolderUpdate {

    private final ExternalStorageAuditLogService externalStorageAuditLogService;

    public FileParentFolderUpdate(
            ExternalStorageAuditLogService externalStorageAuditLogService
    ) {
        this.externalStorageAuditLogService = externalStorageAuditLogService;
    }

    @EventListener(UpdateFileParentFolderEvent.class)
    public void on(UpdateFileParentFolderEvent event) {
        // Create object history for the file parent folder update event


        // Create an audit log entry for the file parent folder update
        externalStorageAuditLogService.createAuditLog(
                event.getUserId(),
                "UPDATE",
                "FILE",
                event.getFileId().toString(),
                "File parent folder updated from " + event.getOldParentFolderName() + " to " + event.getNewParentFolderName()
        );
    }

}
