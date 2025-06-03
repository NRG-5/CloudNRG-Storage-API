package com.cloudnrg.api.storage.application.internal.commandservices;

import com.cloudnrg.api.storage.application.internal.outboundservices.acl.ExternalUserService;
import com.cloudnrg.api.storage.domain.model.aggregates.Folder;
import com.cloudnrg.api.storage.domain.model.commands.CreateDefaultFolderCommand;
import com.cloudnrg.api.storage.domain.model.commands.CreateFolderCommand;
import com.cloudnrg.api.storage.domain.model.commands.UpdateFolderNameComand;
import com.cloudnrg.api.storage.domain.model.commands.UpdateFolderParentCommand;
import com.cloudnrg.api.storage.domain.services.FolderCommandService;
import com.cloudnrg.api.storage.infrastructure.persistence.jpa.repositories.FolderRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FolderCommandServiceImpl implements FolderCommandService {

    private final FolderRepository folderRepository;
    private final ExternalUserService externalUserService;

    public FolderCommandServiceImpl(FolderRepository folderRepository, ExternalUserService externalUserService) {
        this.folderRepository = folderRepository;
        this.externalUserService = externalUserService;
    }

    @Override
    public Optional<Folder> handle(CreateDefaultFolderCommand command) {

        //fetch user from external service
        var user = externalUserService.fetchUserById(command.userId());


        var defaultFolder = new Folder(
                "root",
                null, // Assuming root folder has no parent
                user// User is set from the command
        );

        try {
            // Save the default folder to the repository
            var savedFolder = folderRepository.save(defaultFolder);
            return Optional.of(savedFolder);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Folder> handle(CreateFolderCommand command) {
        return Optional.empty();
    }

    @Override
    public Optional<Folder> handle(UpdateFolderNameComand command) {
        return Optional.empty();
    }

    @Override
    public Optional<Folder> handle(UpdateFolderParentCommand command) {
        return Optional.empty();
    }
}
