package com.cloudnrg.api.storage.application.internal.eventhandlers;

import com.cloudnrg.api.storage.domain.model.events.UpdateFolderParentFolderEvent;
import com.cloudnrg.api.storage.infrastructure.persistence.jpa.repositories.FolderRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UpdateFolderParentFolderEventHandler {
    private final FolderRepository folderRepository;

    @EventListener(UpdateFolderParentFolderEvent.class)
    public void on(UpdateFolderParentFolderEvent event) {
        var getFolderById = folderRepository.findFolderById(event.getFolderId());
        if (getFolderById.isEmpty()) {
            throw new IllegalArgumentException("Folder with ID " + event.getFolderId() + " does not exist.");
        }
        var folder = getFolderById.get();

        var newParentFolderId = folderRepository.findFolderById(event.getNewParentFolderId());
        if (newParentFolderId.isEmpty()) {
            throw new IllegalArgumentException("New parent folder ID cannot be null.");
        }
        var newParentFolder = newParentFolderId.get();

        var oldParentFolderId = folder.getParentFolder();
        if (oldParentFolderId != null && oldParentFolderId.getId().equals(newParentFolderId.get().getId())) {
            throw new IllegalArgumentException("Folder is already in the specified parent folder.");
        }
        folder.setParentFolder(newParentFolder);
        folderRepository.save(folder);
    }
}
