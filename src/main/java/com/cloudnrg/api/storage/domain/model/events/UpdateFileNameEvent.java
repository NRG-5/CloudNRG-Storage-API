package com.cloudnrg.api.storage.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

@Getter
public class UpdateFileNameEvent extends ApplicationEvent {
    private UUID fileId;
    private String newFileName;

    public UpdateFileNameEvent(Object source, UUID fileId, String newFileName) {
        super(source);
        this.fileId = fileId;
        this.newFileName = newFileName;
    }
}
