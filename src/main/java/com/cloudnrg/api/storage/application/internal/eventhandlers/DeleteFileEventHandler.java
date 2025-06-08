package com.cloudnrg.api.storage.application.internal.eventhandlers;

import com.cloudnrg.api.storage.application.internal.outboundservices.acl.ExternalUserService;
import com.cloudnrg.api.storage.domain.model.events.DeleteFileEvent;
import com.cloudnrg.api.storage.infrastructure.persistence.jpa.repositories.CloudFileRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DeleteFileEventHandler {
    private final CloudFileRepository cloudFileRepository;

    @EventListener(DeleteFileEvent.class)
    public void on(DeleteFileEvent event) {
        var getFileById = cloudFileRepository.findById(event.getFileId());
        if (getFileById.isEmpty()) {
            throw new IllegalArgumentException("File with ID " + event.getFileId() + " does not exist.");
        }

        var file = getFileById.get();
        var fileId = file.getId();

        cloudFileRepository.deleteById(fileId);
    }
}
