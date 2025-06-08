package com.cloudnrg.api.storage.application.internal.eventhandlers;

import com.cloudnrg.api.storage.application.internal.outboundservices.acl.ExternalUserService;
import com.cloudnrg.api.storage.domain.model.aggregates.Folder;
import com.cloudnrg.api.storage.domain.model.events.CreateFolderEvent;
import com.cloudnrg.api.storage.infrastructure.persistence.jpa.repositories.CloudFileRepository;
import com.cloudnrg.api.storage.infrastructure.persistence.jpa.repositories.FolderRepository;
import com.cloudnrg.api.storage.interfaces.acl.FolderContextFacade;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreateFolderEventHandler {
    private final FolderContextFacade folderContextFacade;
    private final ExternalUserService externalUserService;
    private final FolderRepository folderRepository;
    private final CloudFileRepository cloudFileRepository;

    @EventListener(CreateFolderEvent.class)
    public void on(CreateFolderEvent event) {
        var user = externalUserService.fetchUserById(event.getUserId());

        var getFolderById = folderRepository.findFolderById(event.getFolderId());
        var folder = getFolderById.get();

        var existingFolder = folderRepository.findFolderByUser_IdAndName(event.getUserId(), folder.getName());

        if (existingFolder.isPresent()) {
            throw new IllegalArgumentException("Folder with the same name already exists for this user.");
        }

        var parentFolder = folder.getParentFolder();

        var newFolder = new Folder(
                folder.getName(),
                parentFolder,
                user
        );
        folderRepository.save(newFolder);
    }
}
