package com.cloudnrg.api.history.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

@Getter
public class CreateObjectHistoryCreateFileEvent extends ApplicationEvent {
    private UUID fileId;
    private UUID userId;

    public CreateObjectHistoryCreateFileEvent(Object source, UUID fileId, UUID userId) {
        super(source);
        this.fileId = fileId;
        this.userId = userId;
    }
}
