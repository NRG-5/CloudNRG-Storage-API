package com.cloudnrg.api.auditlog.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import java.util.UUID;

@Getter
public class UserUpdatedFolderEvent extends ApplicationEvent {
    private final UUID userId;
    private final UUID folderId;
    private final String newName;

    public UserUpdatedFolderEvent(Object source, UUID userId, UUID folderId, String newName) {
        super(source);
        this.userId = userId;
        this.folderId = folderId;
        this.newName = newName;
    }
}
