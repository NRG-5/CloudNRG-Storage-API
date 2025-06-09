package com.cloudnrg.api.history.application.internal.eventhandlers;

import com.cloudnrg.api.history.domain.model.aggregates.ObjectHistory;
import com.cloudnrg.api.history.domain.model.events.SaveObjectHistoryUpdateFileParentFolderEvent;
import com.cloudnrg.api.history.domain.model.valueobjects.Action;
import com.cloudnrg.api.history.infrastructure.persistence.jpa.repositories.ObjectHistoryRepository;
import com.cloudnrg.api.storage.infrastructure.persistence.jpa.repositories.CloudFileRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SaveObjectHistoryUpdateFileParentFolderEventHandler {
    private final ObjectHistoryRepository objectHistoryRepository;
    private final CloudFileRepository cloudFileRepository;

    @EventListener(SaveObjectHistoryUpdateFileParentFolderEvent.class)
    public void on(SaveObjectHistoryUpdateFileParentFolderEvent event) {
        var file = cloudFileRepository.findById(event.getFileId())
                .orElseThrow(() -> new IllegalArgumentException("File not found with ID: " + event.getFileId()));

        var user = file.getUser();

        var action = Action.UPDATE;

        var objectHistory = new ObjectHistory(
                file,
                user,
                action
        );
        objectHistoryRepository.save(objectHistory);
        System.out.println("Action: File Parent Folder Updated" + event.getAllDetails());
    }
}
