package com.cloudnrg.api.storage.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

@Getter
public class UpdateFolderParentFolderEvent extends ApplicationEvent {
    private UUID folderId;
    private UUID oldParentFolderId;
    private UUID newParentFolderId;

    public UpdateFolderParentFolderEvent(Object source, UUID folderId, UUID oldParentFolderId, UUID newParentFolderId) {
        super(source);
        this.folderId = folderId;
        this.oldParentFolderId = oldParentFolderId;
        this.newParentFolderId = newParentFolderId;
    }
}
