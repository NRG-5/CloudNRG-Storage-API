package com.cloudnrg.api.storage.domain.services;

import com.cloudnrg.api.storage.domain.model.aggregates.CloudFile;
import com.cloudnrg.api.storage.domain.model.command.CreateFileCommand;

import java.util.Optional;

public interface FileCommandService {

    Optional<CloudFile> handle(CreateFileCommand command);
}
