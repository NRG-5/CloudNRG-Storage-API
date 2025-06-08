package com.cloudnrg.api.storage.interfaces.acl;

import com.cloudnrg.api.storage.domain.model.aggregates.Folder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FolderContextFacade {

    UUID createDefaultFolder(UUID userId);
    UUID createFolder(UUID userId, String name, UUID parentFolderId);
    void deleteFolderById(UUID folderId);
    void updateFolderName(UUID folderId, String newName);
    void updateFolderParentFolder(UUID folderId, UUID newParentFolderId);
}
