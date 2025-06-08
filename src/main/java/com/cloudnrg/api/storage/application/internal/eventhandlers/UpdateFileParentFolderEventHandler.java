package com.cloudnrg.api.storage.application.internal.eventhandlers;

import com.cloudnrg.api.storage.domain.model.events.UpdateFileParentFolderEvent;
import com.cloudnrg.api.storage.infrastructure.persistence.jpa.repositories.CloudFileRepository;
import com.cloudnrg.api.storage.infrastructure.persistence.jpa.repositories.FolderRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UpdateFileParentFolderEventHandler {
    private final CloudFileRepository cloudFileRepository;
    private final FolderRepository folderRepository;

    @EventListener(UpdateFileParentFolderEvent.class)
    public void on(UpdateFileParentFolderEvent event) {
        var getFileById = cloudFileRepository.findById(event.getFileId());
        if (getFileById.isEmpty()) {
            throw new IllegalArgumentException("File with ID " + event.getFileId() + " does not exist.");
        }
        var file = getFileById.get();

        var newParentFolderId = folderRepository.findById(event.getNewParentFolderId());
        if (newParentFolderId == null) {
            throw new IllegalArgumentException("New parent folder ID cannot be null.");
        }
        var newParentFolder = newParentFolderId.get();

        var oldParentFolderId = file.getFolder();
        if (oldParentFolderId != null && oldParentFolderId.getId().equals(newParentFolderId)) {
            throw new IllegalArgumentException("File is already in the specified parent folder.");
        }
        file.setFolder(newParentFolder);
        cloudFileRepository.save(file);
    }
}
