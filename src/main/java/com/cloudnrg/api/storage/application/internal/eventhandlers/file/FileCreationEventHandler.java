package com.cloudnrg.api.storage.application.internal.eventhandlers.file;

import com.cloudnrg.api.storage.application.internal.outboundservices.acl.ExternalStorageAuditLogService;
import com.cloudnrg.api.storage.domain.model.events.CreateFileEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class FileCreationEventHandler {
    private final ExternalStorageAuditLogService externalStorageAuditLogService;

    public FileCreationEventHandler(
            ExternalStorageAuditLogService externalStorageAuditLogService
    ) {
        this.externalStorageAuditLogService = externalStorageAuditLogService;
    }

    @EventListener(CreateFileEvent.class)
    public void on(CreateFileEvent event) {
        // Create object history for the file creation event

        // Create an audit log entry for the file creation
        externalStorageAuditLogService.createAuditLog(
                event.getUserId(),
                "CREATE",
                "FILE",
                event.getFileId().toString(),
                "File created with ID: " + event.getFileId()
        );
    }

}
