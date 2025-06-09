package com.cloudnrg.api.history.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

@Getter
public class SaveObjectHistoryUpdateFileParentFolderEvent extends ApplicationEvent {
    private UUID fileId;
    private UUID oldParentFolderId;
    private String newParentFolderId;
    private String details;

    public SaveObjectHistoryUpdateFileParentFolderEvent(Object source, UUID fileId, UUID oldParentFolderId, String newParentFolderId, String action, String details) {
        super(source);
        this.fileId = fileId;
        this.oldParentFolderId = oldParentFolderId;
        this.newParentFolderId = newParentFolderId;
        this.details = details;
    }

    public String getAllDetails() {
        return String.format("File ID: %s, Old Parent Folder ID: %s, New Parent Folder ID: %s",
                             fileId, oldParentFolderId, newParentFolderId);
    }
}
