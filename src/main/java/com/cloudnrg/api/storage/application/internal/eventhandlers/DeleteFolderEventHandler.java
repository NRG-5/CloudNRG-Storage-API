package com.cloudnrg.api.storage.application.internal.eventhandlers;

import com.cloudnrg.api.storage.application.internal.outboundservices.acl.ExternalUserService;
import com.cloudnrg.api.storage.domain.model.events.DeleteFolderEvent;
import com.cloudnrg.api.storage.infrastructure.persistence.jpa.repositories.FolderRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DeleteFolderEventHandler {
    private final FolderRepository folderRepository;

    @EventListener(DeleteFolderEvent.class)
    public void on(DeleteFolderEvent event) {
        var getFolderById = folderRepository.findFolderById(event.getFolderId());
        if (getFolderById.isEmpty()) {
            throw new IllegalArgumentException("Folder with ID " + event.getFolderId() + " does not exist.");
        }

        var folder = getFolderById.get();
        var folderId = folder.getId();

        folderRepository.deleteById(folderId);
    }
}
