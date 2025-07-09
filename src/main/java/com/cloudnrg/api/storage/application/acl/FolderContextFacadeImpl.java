package com.cloudnrg.api.storage.application.acl;

import com.cloudnrg.api.storage.domain.model.aggregates.Folder;
import com.cloudnrg.api.storage.domain.model.commands.CreateDefaultFolderCommand;
import com.cloudnrg.api.storage.domain.model.queries.GetAllFoldersByUserIdQuery;
import com.cloudnrg.api.storage.domain.services.FolderCommandService;
import com.cloudnrg.api.storage.domain.services.FolderQueryService;
import com.cloudnrg.api.storage.interfaces.acl.FolderContextFacade;
import jakarta.persistence.GenerationType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FolderContextFacadeImpl implements FolderContextFacade {

    private final FolderCommandService folderCommandService;
    private final FolderQueryService folderQueryService;

    public FolderContextFacadeImpl(FolderCommandService folderCommandService, FolderQueryService folderQueryService) {
        this.folderCommandService = folderCommandService;
        this.folderQueryService = folderQueryService;
    }

    @Override
    public UUID createDefaultFolder(UUID userId) {

        var defaultFolder = folderCommandService.handle(new CreateDefaultFolderCommand(userId));

        if (defaultFolder.isEmpty()) {
            throw new RuntimeException("Failed to create default folder");
        }

        return defaultFolder.get().getId();
    }

    @Override
    public List<Folder> fetchAllFoldersByUserId(UUID userId) {
        return folderQueryService.handle(new GetAllFoldersByUserIdQuery(userId));
    }
}
