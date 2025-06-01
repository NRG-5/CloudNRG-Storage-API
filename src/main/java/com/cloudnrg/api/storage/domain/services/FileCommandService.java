package com.cloudnrg.api.storage.domain.services;

import com.cloudnrg.api.storage.domain.model.aggregates.CloudFile;
import com.cloudnrg.api.storage.domain.model.commands.CreateFileCommand;
import com.cloudnrg.api.storage.domain.model.commands.DeleteFileByIdCommand;

import java.util.Optional;

public interface FileCommandService {

    Optional<CloudFile> handle(CreateFileCommand command);
    void handle(DeleteFileByIdCommand command);
}
