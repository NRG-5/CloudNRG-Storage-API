package com.cloudnrg.api.history.application.internal.eventhandlers;

import com.cloudnrg.api.history.domain.model.events.DeleteAllObjectsHistoryEvent;
import com.cloudnrg.api.history.infrastructure.persistence.jpa.repositories.ObjectHistoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DeleteAllObjectsHistoryEventHandler {
    private final ObjectHistoryRepository repository;

    @EventListener(DeleteAllObjectsHistoryEvent.class)
    public void on(DeleteAllObjectsHistoryEvent event) {
        repository.deleteAllByFile_Id(event.getFileId());
    }
}
