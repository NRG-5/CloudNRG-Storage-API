package com.cloudnrg.api.history.application.internal.eventhandlers;

import com.cloudnrg.api.history.application.internal.outboundservices.ExternalFileService;
import com.cloudnrg.api.history.domain.model.aggregates.ObjectHistory;
import com.cloudnrg.api.history.domain.model.events.CreateObjectHistoryUpdateFileParentFolderEvent;
import com.cloudnrg.api.history.domain.model.valueobjects.Action;
import com.cloudnrg.api.history.infrastructure.persistence.jpa.repositories.ObjectHistoryRepository;
import com.cloudnrg.api.storage.infrastructure.persistence.jpa.repositories.CloudFileRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreateObjectHistoryUpdateFileParentFolderEventHandler {
    private final ObjectHistoryRepository objectHistoryRepository;
    private final ExternalFileService externalFileService;

    @EventListener(CreateObjectHistoryUpdateFileParentFolderEvent.class)
    public void on(CreateObjectHistoryUpdateFileParentFolderEvent event) {
        var file = externalFileService.fetchFileById(event.getFileId());

        var user = file.getUser();

        var action = Action.UPDATE_FILE_PARENT_FOLDER;

        var objectHistory = new ObjectHistory(
                file,
                user,
                action
        );
        objectHistoryRepository.save(objectHistory);
    }
}
