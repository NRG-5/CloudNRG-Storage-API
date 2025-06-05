package com.cloudnrg.api.storage.domain.services;

import com.cloudnrg.api.storage.domain.model.aggregates.Folder;
import com.cloudnrg.api.storage.domain.model.commands.*;

import java.util.Optional;

public interface FolderCommandService {
    Optional<Folder> handle(CreateDefaultFolderCommand command);
    Optional<Folder> handle(CreateFolderCommand command);
    Optional<Folder> handle(UpdateFolderNameCommand command);
    Optional<Folder> handle(UpdateFolderParentCommand command);
    void handle(DeleteFolderByIdCommand command);
}
