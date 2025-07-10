package com.cloudnrg.api.storage.application.internal.eventhandlers.folder;

import com.cloudnrg.api.shared.application.external.outboundedservices.ExternalAuditLogService;
import com.cloudnrg.api.storage.domain.model.commands.DeleteFileByIdCommand;
import com.cloudnrg.api.storage.domain.model.events.DeleteFolderEvent;
import com.cloudnrg.api.storage.domain.model.queries.GetFilesByFolderIdQuery;
import com.cloudnrg.api.storage.domain.services.FileCommandService;
import com.cloudnrg.api.storage.domain.services.FileQueryService;
import com.cloudnrg.api.storage.infrastructure.persistence.jpa.repositories.CloudFileRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class FolderDeletionEventHandler {

    private final ExternalAuditLogService externalStorageAuditLogService;
    private final FileCommandService fileCommandService;
    private final FileQueryService fileQueryService;

    public FolderDeletionEventHandler(
            ExternalAuditLogService externalStorageAuditLogService,
            FileCommandService fileCommandService,
            FileQueryService fileQueryService,
            CloudFileRepository cloudFileRepository) {
        this.externalStorageAuditLogService = externalStorageAuditLogService;
        this.fileCommandService = fileCommandService;
        this.fileQueryService = fileQueryService;
    }

    @EventListener(DeleteFolderEvent.class)
    public void on(DeleteFolderEvent event) {
        //delete all the files in the folder
        var filesToDelete =  fileQueryService.handle(new GetFilesByFolderIdQuery(event.getFolderId()));

        filesToDelete.forEach(file -> {
            fileCommandService.handle(new DeleteFileByIdCommand(file.getId()));
        });

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
