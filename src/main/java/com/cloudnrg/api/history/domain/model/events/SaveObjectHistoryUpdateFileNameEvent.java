package com.cloudnrg.api.history.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

@Getter
public class SaveObjectHistoryUpdateFileNameEvent extends ApplicationEvent {
    private UUID fileId;
    private String newFileName;
    private String details;

    public SaveObjectHistoryUpdateFileNameEvent(Object source, UUID fileId, String newFileName, String details) {
        super(source);
        this.fileId = fileId;
        this.newFileName = newFileName;
        this.details = details;
    }

    public String getAllDetails() {
        return String.format("File ID: %s, New File Name: %s",
                             fileId, newFileName);
    }
}
