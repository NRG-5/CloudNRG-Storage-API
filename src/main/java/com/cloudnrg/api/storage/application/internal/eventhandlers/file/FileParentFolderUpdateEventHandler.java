package com.cloudnrg.api.storage.application.internal.eventhandlers.file;

import com.cloudnrg.api.shared.application.external.outboundedservices.ExternalAuditLogService;
import com.cloudnrg.api.storage.application.internal.outboundservices.acl.ExternalObjectHistoryService;
import com.cloudnrg.api.storage.domain.model.events.UpdateFileParentFolderEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class FileParentFolderUpdateEventHandler {
    private final ExternalObjectHistoryService externalObjectHistoryService;
    private final ExternalAuditLogService externalAuditLogService;

    public FileParentFolderUpdateEventHandler(ExternalObjectHistoryService externalObjectHistoryService, ExternalAuditLogService externalAuditLogService) {
        this.externalObjectHistoryService = externalObjectHistoryService;
        this.externalAuditLogService = externalAuditLogService;
    }


    @EventListener(UpdateFileParentFolderEvent.class)
    public void on(UpdateFileParentFolderEvent event) {
        externalObjectHistoryService.saveObjectHistoryUpdateFileParentFolder(event.getFileId(), event.getUserId(), event.getNewParentFolderId());

        externalAuditLogService.createAuditLog(
                event.getUserId(),
                "UPDATE",
                "FILE",
                event.getFileId().toString(),
                "File parent folder updated from " + event.getOldParentFolderName() + " to " + event.getNewParentFolderName()
        );
    }

}
