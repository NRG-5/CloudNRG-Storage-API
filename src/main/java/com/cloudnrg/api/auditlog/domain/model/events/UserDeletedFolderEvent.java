package com.cloudnrg.api.auditlog.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import java.util.UUID;


@Getter
public class UserDeletedFolderEvent extends ApplicationEvent {
    private final UUID userId;
    private final UUID folderId;
    private final String folderName;

    public UserDeletedFolderEvent(Object source, UUID userId, UUID folderId, String folderName) {
        super(source);
        this.userId = userId;
        this.folderId = folderId;
        this.folderName = folderName;
    }

}
