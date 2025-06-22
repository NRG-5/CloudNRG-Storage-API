package com.cloudnrg.api.history.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

@Getter
public class CreateObjectHistoryUpdateFileParentFolderEvent extends ApplicationEvent {
    private UUID fileId;
    private UUID userId;
    private UUID oldParentFolderId;
    private String newParentFolderId;

    public CreateObjectHistoryUpdateFileParentFolderEvent(Object source, UUID fileId, UUID userId, UUID oldParentFolderId, String newParentFolderId) {
        super(source);
        this.fileId = fileId;
        this.userId = userId;
        this.oldParentFolderId = oldParentFolderId;
        this.newParentFolderId = newParentFolderId;
    }
}
