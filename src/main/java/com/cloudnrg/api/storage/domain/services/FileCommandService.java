package com.cloudnrg.api.storage.domain.services;

import com.cloudnrg.api.storage.domain.model.aggregates.CloudFile;
import com.cloudnrg.api.storage.domain.model.commands.*;

import java.util.Optional;

public interface FileCommandService {
    Optional<CloudFile> handle(CreateFileCommand command);
    void handle(DeleteFileByIdCommand command);
    Optional<CloudFile> handle(UpdateFileFolderCommand command);
    Optional<CloudFile> handle(UpdateFileNameCommand command);
}
