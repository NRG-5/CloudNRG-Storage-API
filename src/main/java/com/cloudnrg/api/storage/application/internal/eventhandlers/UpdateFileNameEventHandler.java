package com.cloudnrg.api.storage.application.internal.eventhandlers;

import com.cloudnrg.api.storage.domain.model.events.UpdateFileNameEvent;
import com.cloudnrg.api.storage.infrastructure.persistence.jpa.repositories.CloudFileRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UpdateFileNameEventHandler {
    private final CloudFileRepository cloudFileRepository;

    @EventListener(UpdateFileNameEvent.class)
    public void on(UpdateFileNameEvent event) {
        var getFileById = cloudFileRepository.findById(event.getFileId());
        if (getFileById.isEmpty()) {
            throw new IllegalArgumentException("File with ID " + event.getFileId() + " does not exist.");
        }
        var file = getFileById.get();
        file.setFilename(event.getNewFileName());
        cloudFileRepository.save(file);
    }
}
