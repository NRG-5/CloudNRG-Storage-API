package com.cloudnrg.api.auditlog.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import java.util.UUID;

@Getter
public class UserUploadedFileEvent extends ApplicationEvent {
    private final UUID userId;
    private final UUID fileId;
    private final String fileName;

    public UserUploadedFileEvent(Object source, UUID userId, UUID fileId, String fileName) {
        super(source);
        this.userId = userId;
        this.fileId = fileId;
        this.fileName = fileName;
    }
}
