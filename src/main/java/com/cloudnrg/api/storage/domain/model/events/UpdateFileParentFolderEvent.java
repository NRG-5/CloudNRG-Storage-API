package com.cloudnrg.api.storage.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

@Getter
public class UpdateFileParentFolderEvent extends ApplicationEvent {
    private UUID fileId;
    private UUID oldParentFolderId;
    private UUID newParentFolderId;

    public UpdateFileParentFolderEvent(Object source, UUID fileId, UUID oldParentFolderId, UUID newParentFolderId) {
        super(source);
        this.fileId = fileId;
        this.oldParentFolderId = oldParentFolderId;
        this.newParentFolderId = newParentFolderId;
    }
}
