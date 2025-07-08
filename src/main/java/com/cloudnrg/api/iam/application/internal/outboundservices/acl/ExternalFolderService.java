package com.cloudnrg.api.iam.application.internal.outboundservices.acl;

import com.cloudnrg.api.storage.domain.model.aggregates.Folder;
import com.cloudnrg.api.storage.interfaces.acl.FolderContextFacade;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ExternalFolderService {

    private final FolderContextFacade folderContextFacade;

    public ExternalFolderService(FolderContextFacade folderContextFacade) {
        this.folderContextFacade = folderContextFacade;
    }

    public UUID createRootFolderForUser(UUID userId) {
        return folderContextFacade.createDefaultFolder(userId);
    }

    public List<Folder> fetchAllFoldersByUserId(UUID userId) {
        var folders = folderContextFacade.fetchAllFoldersByUserId(userId);
        if (folders.isEmpty()) {
            throw new IllegalArgumentException("No folders found for user with ID " + userId);
        }
        return folders;
    }
}
