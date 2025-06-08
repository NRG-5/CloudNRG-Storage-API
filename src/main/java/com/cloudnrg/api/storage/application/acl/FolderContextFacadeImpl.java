package com.cloudnrg.api.storage.application.acl;

import com.cloudnrg.api.storage.domain.model.aggregates.Folder;
import com.cloudnrg.api.storage.domain.model.commands.*;
import com.cloudnrg.api.storage.domain.services.FolderCommandService;
import com.cloudnrg.api.storage.domain.services.FolderQueryService;
import com.cloudnrg.api.storage.interfaces.acl.FolderContextFacade;
import lombok.AllArgsConstructor;
import org.hibernate.sql.Update;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class FolderContextFacadeImpl implements FolderContextFacade {
    private final FolderCommandService folderCommandService;
    private final FolderQueryService folderQueryService;

    @Override
    public UUID createDefaultFolder(UUID userId) {

        var defaultFolder = folderCommandService.handle(new CreateDefaultFolderCommand(userId));

        if (defaultFolder.isEmpty()) {
            throw new RuntimeException("Failed to create default folder");
        }

        return defaultFolder.get().getId();
    }

    @Override
    public UUID createFolder(UUID userId, String name, UUID parentFolderId) {
        var folder = folderCommandService.handle(new CreateFolderCommand(userId, name, parentFolderId));

        if (folder.isEmpty()) {
            throw new RuntimeException("Failed to create folder");
        }

        return folder.get().getId();
    }

    @Override
    public void deleteFolderById(UUID folderId) {
        folderCommandService.handle(new DeleteFolderByIdCommand(folderId));
    }

    @Override
    public void updateFolderName(UUID folderId, String newName) {
        folderCommandService.handle(new UpdateFolderNameCommand(folderId, newName));
    }

    @Override
    public void updateFolderParentFolder(UUID folderId, UUID newParentFolderId) {
        folderCommandService.handle(new UpdateFolderParentCommand(folderId, newParentFolderId));
    }

}
