package com.cloudnrg.api.history.application.internal.eventhandlers;

import com.cloudnrg.api.history.application.internal.outboundservices.ExternalFileService;
import com.cloudnrg.api.history.domain.model.aggregates.ObjectHistory;
import com.cloudnrg.api.history.domain.model.events.CreateObjectHistoryCreateFileEvent;
import com.cloudnrg.api.history.domain.model.valueobjects.Action;
import com.cloudnrg.api.history.infrastructure.persistence.jpa.repositories.ObjectHistoryRepository;
import com.cloudnrg.api.storage.infrastructure.persistence.jpa.repositories.CloudFileRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreateObjectHistoryCreateFileEventHandler {
    private final ObjectHistoryRepository objectHistoryRepository;
    private final ExternalFileService externalFileService;

    @EventListener(CreateObjectHistoryCreateFileEvent.class)
    public void on(CreateObjectHistoryCreateFileEvent event) {
        var file = externalFileService.fetchFileById(event.getFileId());

        var user = file.getUser();

        var action = Action.CREATE_FILE;

        var objectHistory = new ObjectHistory(
                file,
                user,
                action
        );
        objectHistoryRepository.save(objectHistory);
    }
}
