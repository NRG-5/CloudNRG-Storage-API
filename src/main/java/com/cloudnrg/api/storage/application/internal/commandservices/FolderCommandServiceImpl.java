package com.cloudnrg.api.storage.application.internal.commandservices;

import com.cloudnrg.api.storage.application.internal.outboundservices.acl.ExternalUserService;
import com.cloudnrg.api.storage.domain.model.aggregates.Folder;
import com.cloudnrg.api.storage.domain.model.commands.*;
import com.cloudnrg.api.storage.domain.model.events.*;
import com.cloudnrg.api.storage.domain.model.queries.GetFolderAscendantHierarchyQuery;
import com.cloudnrg.api.storage.domain.services.FolderCommandService;
import com.cloudnrg.api.storage.domain.services.FolderQueryService;
import com.cloudnrg.api.storage.infrastructure.persistence.jpa.repositories.FolderRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class FolderCommandServiceImpl implements FolderCommandService {

    private final FolderRepository folderRepository;
    private final ExternalUserService externalUserService;
    private final FolderQueryService queryService;
    private final ApplicationEventPublisher eventPublisher;

    public FolderCommandServiceImpl(FolderRepository folderRepository, ExternalUserService externalUserService, FolderQueryService queryService, ApplicationEventPublisher eventPublisher) {
        this.folderRepository = folderRepository;
        this.externalUserService = externalUserService;
        this.queryService = queryService;
        this.eventPublisher = eventPublisher;
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

            // Publish an event after saving the folder
            eventPublisher.publishEvent(new CreateFileEvent(defaultFolder, savedFolder.getId(), user.getId()));

            return Optional.of(savedFolder);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public Optional<Folder> handle(CreateFolderCommand command) {
        var user = externalUserService.fetchUserById(command.userId());

        // Check if a folder with the same name already exists for the user
        var existingFolder = folderRepository.findFolderByUser_IdAndName(user.getId(), command.folderName());
        if (existingFolder.isPresent()) {
            throw new RuntimeException("Folder with the same name already exists for this user.");
        }

        var folder = folderRepository.findFolderById(command.parentFolderId());

        var newFolder = new Folder(
                command.folderName(),
                folder.get(),
                user
        );

        try {
            // Save the new folder to the repository
            var savedFolder = folderRepository.save(newFolder);

            // Publish an event after saving the folder
            eventPublisher.publishEvent(new CreateFolderEvent(newFolder, savedFolder.getId(), user.getId()));

            return Optional.of(savedFolder);
        } catch (Exception e) {
            throw new RuntimeException("Error creating folder: " + e.getMessage());
        }
    }


    @Override
    public Optional<Folder> handle(UpdateFolderNameCommand command) {
        var folderResult = folderRepository.findFolderById(command.folderId());

        if (folderResult.isEmpty()) {
            throw new RuntimeException("Folder not found");
        }

        var existingFolder = folderRepository.findFolderByUser_IdAndName(folderResult.get().getUser().getId(), command.name());
        if (existingFolder.isPresent() && !existingFolder.get().getId().equals(command.folderId())) {
            throw new RuntimeException("Folder with the same name already exists for this user.");
        }

        var folder = folderResult.get();
        folder.setName(command.name());

        try {
            folderRepository.save(folder);

            // Publish an event after updating the folder
            eventPublisher.publishEvent(new UpdateFolderNameEvent(folder, folder.getId(), command.name()));

            return Optional.of(folder);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update folder name: " + e.getMessage());
        }
    }


    @Override
    public Optional<Folder> handle(UpdateFolderParentCommand command) {
        var folderResult = folderRepository.findFolderById(command.folderId());

        if (folderResult.isEmpty()) {
            throw new RuntimeException("Folder not found");
        }

        var folder = folderResult.get();
        var oldParentFolder = folder.getParentFolder();
        var newParentFolderResult = folderRepository.findFolderById(command.parentFolderId());

        if (newParentFolderResult.isEmpty()) {
            throw new RuntimeException("New parent folder not found");
        }

        var newParentFolder = newParentFolderResult.get();

        if (isDescendant(folder.getId(), command.parentFolderId())) {
            throw new RuntimeException("Cannot set a folder as its own parent or a descendant of itself.");
        }

        folder.setParentFolder(newParentFolder);

        try {
            folderRepository.save(folder);

            // Publish an event after updating the folder's parent
            eventPublisher.publishEvent(new UpdateFolderParentFolderEvent(folder, folder.getId(), oldParentFolder.getId(), newParentFolder.getId()));

            return Optional.of(folder);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update folder parent: " + e.getMessage());
        }
    }


    @Override
    public void handle(DeleteFolderByIdCommand command) {
        var folder = folderRepository.findFolderById(command.folderId());

        if (folder.isEmpty()) {
            throw new RuntimeException("Folder not found");
        }

        try {
            folderRepository.delete(folder.get());

            // Publish an event after deleting the folder
            eventPublisher.publishEvent(new DeleteFolderEvent(folder, folder.get().getId()));
        } catch (Exception e) {
            throw new RuntimeException("Error deleting folder: " + e.getMessage());
        }
    }

    private boolean isDescendant(UUID folderId, UUID newParentId) {
        var hierarchyResult = queryService.handle(new GetFolderAscendantHierarchyQuery(newParentId));
        if (hierarchyResult.isEmpty()) return false;
        return hierarchyResult.get().stream().anyMatch(folder -> folder.getId().equals(folderId));
    }
}
