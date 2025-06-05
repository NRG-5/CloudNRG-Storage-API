package com.cloudnrg.api.storage.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

@Getter
public class DeleteFolderEvent extends ApplicationEvent {
    private UUID folderId;

    public DeleteFolderEvent(Object source, UUID folderId) {
        super(source);
        this.folderId = folderId;
    }
}
