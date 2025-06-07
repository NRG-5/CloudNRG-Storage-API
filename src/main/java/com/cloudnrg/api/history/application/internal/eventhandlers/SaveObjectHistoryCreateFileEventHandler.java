package com.cloudnrg.api.history.application.internal.eventhandlers;

import com.cloudnrg.api.history.domain.model.aggregates.ObjectHistory;
import com.cloudnrg.api.history.domain.model.events.SaveObjectHistoryCreateFileEvent;
import com.cloudnrg.api.history.domain.model.valueobjects.Action;
import com.cloudnrg.api.history.infrastructure.persistence.jpa.repositories.ObjectHistoryRepository;
import com.cloudnrg.api.iam.infrastructure.persistance.jpa.repositories.UserRepository;
import com.cloudnrg.api.storage.infrastructure.persistence.jpa.repositories.CloudFileRepository;
import com.cloudnrg.api.storage.infrastructure.persistence.jpa.repositories.FolderRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SaveObjectHistoryCreateFileEventHandler {
    private final ObjectHistoryRepository objectHistoryRepository;
    private final CloudFileRepository cloudFileRepository;

    @EventListener(SaveObjectHistoryCreateFileEvent.class)
    public void on(SaveObjectHistoryCreateFileEvent event) {
        var file = cloudFileRepository.findById(event.getFileId())
                .orElseThrow(() -> new IllegalArgumentException("File not found with ID: " + event.getFileId()));

        var user = file.getUser();

        var action = Action.CREATE;

        var objectHistory = new ObjectHistory(
                file,
                user,
                action
        );
        objectHistoryRepository.save(objectHistory);
        System.out.println("Action: File Created" + event.getAllDetails());
    }
}
