package com.cloudnrg.api.storage.application.acl;

import com.cloudnrg.api.storage.domain.model.commands.CreateDefaultFolderCommand;
import com.cloudnrg.api.storage.domain.services.FolderCommandService;
import com.cloudnrg.api.storage.interfaces.acl.FolderContextFacade;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FolderContextFacadeImpl implements FolderContextFacade {

    private final FolderCommandService folderCommandService;

    public FolderContextFacadeImpl(FolderCommandService folderCommandService) {
        this.folderCommandService = folderCommandService;
    }

    @Override
    public UUID createDefaultFolder(UUID userId) {

        var defaultFolder = folderCommandService.handle(new CreateDefaultFolderCommand(userId));

        if (defaultFolder.isEmpty()) {
            throw new RuntimeException("Failed to create default folder");
        }

        return defaultFolder.get().getId();
    }

}
