package com.cloudnrg.api.storage.application.internal.eventhandlers.file;

import com.cloudnrg.api.storage.application.internal.outboundservices.acl.ExternalStorageAuditLogService;
import com.cloudnrg.api.storage.domain.model.events.UpdateFileNameEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class FileNameUpdateEventHandler {

    private final ExternalStorageAuditLogService externalStorageAuditLogService;

    public FileNameUpdateEventHandler(
            ExternalStorageAuditLogService externalStorageAuditLogService
    ) {
        this.externalStorageAuditLogService = externalStorageAuditLogService;
    }

    @EventListener(UpdateFileNameEvent.class)
    public void on(UpdateFileNameEvent event) {
        // Create object history for the file name update event


        // Create an audit log entry for the file name update
        externalStorageAuditLogService.createAuditLog(
                event.getUserId(),
                "UPDATE",
                "FILE",
                event.getFileId().toString(),
                "File name updated from " + event.getOldFileName() + " to " + event.getNewFileName()
        );
    }

}
