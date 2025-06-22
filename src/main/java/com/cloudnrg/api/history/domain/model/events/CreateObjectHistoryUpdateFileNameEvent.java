package com.cloudnrg.api.history.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

@Getter
public class CreateObjectHistoryUpdateFileNameEvent extends ApplicationEvent {
    private UUID fileId;
    private UUID userId;
    private String oldFileName;
    private String newFileName;

    public CreateObjectHistoryUpdateFileNameEvent(Object source, UUID fileId, UUID userId, String oldFileName, String newFileName) {
        super(source);
        this.fileId = fileId;
        this.userId = userId;
        this.oldFileName = oldFileName;
        this.newFileName = newFileName;
    }
}
