package com.cloudnrg.api.history.domain.model.events;

import com.cloudnrg.api.history.domain.model.valueobjects.Action;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

@Getter
public class SaveObjectHistoryCreateFileEvent extends ApplicationEvent {
    private UUID fileId;
    private UUID userId;
    private String details;

    public SaveObjectHistoryCreateFileEvent(Object source, UUID fileId, UUID userId, String details) {
        super(source);
        this.fileId = fileId;
        this.userId = userId;
        this.details = details;
    }

    public String getAllDetails() {
        return String.format("File ID: %s, User ID: %s",
                             fileId, userId);
    }
}
