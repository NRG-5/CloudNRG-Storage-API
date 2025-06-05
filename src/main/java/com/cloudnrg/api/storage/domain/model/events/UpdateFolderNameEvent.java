package com.cloudnrg.api.storage.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

@Getter
public class UpdateFolderNameEvent extends ApplicationEvent {
    private UUID folderId;
    private String newName;

    public UpdateFolderNameEvent(Object source, UUID folderId, String newName) {
        super(source);
        this.folderId = folderId;
        this.newName = newName;
    }
}
