package com.cloudnrg.api.storage.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

@Getter
public class CreateFileEvent extends ApplicationEvent {
    private UUID fileId;
    private UUID userId;

    public CreateFileEvent(Object source, UUID fileId, UUID userId) {
        super(source);
        this.fileId = fileId;
        this.userId = userId;
    }
}
