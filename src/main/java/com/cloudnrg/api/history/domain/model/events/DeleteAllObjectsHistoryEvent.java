package com.cloudnrg.api.history.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

@Getter
public class DeleteAllObjectsHistoryEvent extends ApplicationEvent {
    private UUID fileId;

    public DeleteAllObjectsHistoryEvent(Object source, UUID fileId) {
        super(source);
        this.fileId = fileId;
    }
}
