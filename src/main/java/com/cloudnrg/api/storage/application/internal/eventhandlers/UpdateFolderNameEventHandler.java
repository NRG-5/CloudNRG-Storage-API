package com.cloudnrg.api.storage.application.internal.eventhandlers;

import com.cloudnrg.api.storage.domain.model.events.UpdateFolderNameEvent;
import com.cloudnrg.api.storage.infrastructure.persistence.jpa.repositories.FolderRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UpdateFolderNameEventHandler {
    private final FolderRepository folderRepository;

    @EventListener(UpdateFolderNameEvent.class)
    public void on(UpdateFolderNameEvent event) {
        var getFolderById = folderRepository.findFolderById(event.getFolderId());
        if (getFolderById.isEmpty()) {
            throw new IllegalArgumentException("Folder with ID " + event.getFolderId() + " does not exist.");
        }
        var folder = getFolderById.get();

        folder.setName(event.getNewName());
        folderRepository.save(folder);
    }
}
