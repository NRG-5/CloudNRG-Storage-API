package com.cloudnrg.api.history.application.internal.commandservices;

import com.cloudnrg.api.history.application.internal.outboundservices.ExternalFileService;
import com.cloudnrg.api.history.domain.model.aggregates.ObjectHistory;
import com.cloudnrg.api.history.domain.model.commands.CreateObjectHistoryCommand;
import com.cloudnrg.api.history.domain.model.commands.DeleteAllObjectsHistoryByFileIdCommand;
import com.cloudnrg.api.history.domain.services.ObjectHistoryCommandService;
import com.cloudnrg.api.history.infrastructure.persistence.jpa.repositories.ObjectHistoryRepository;
import com.cloudnrg.api.iam.infrastructure.persistance.jpa.repositories.UserRepository;
import com.cloudnrg.api.storage.application.internal.outboundservices.acl.ExternalUserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ObjectHistoryCommandServiceImpl implements ObjectHistoryCommandService {
    private final ObjectHistoryRepository objectHistoryRepository;
    private final ExternalUserService externalUserService;
    private final ExternalFileService externalFileService;

    public ObjectHistoryCommandServiceImpl(ObjectHistoryRepository objectHistoryRepository, ExternalUserService externalUserService, UserRepository userRepository, ExternalFileService externalFileService) {
        this.objectHistoryRepository = objectHistoryRepository;
        this.externalUserService = externalUserService;
        this.externalFileService = externalFileService;
    }

    @Override
    public Optional<ObjectHistory> handle(CreateObjectHistoryCommand command) {
        var file = externalFileService.fetchFileById(command.fileId());
        var user = externalUserService.fetchUserById(command.userId());
        if (file == null) {
            throw new IllegalArgumentException("File not found for ID: " + command.fileId());
        }

        var objectHistory = new ObjectHistory(
                file,
                user,
                command.action()
        );

        try {
            var savedObjectHistory = objectHistoryRepository.save(objectHistory);
            return Optional.of(savedObjectHistory);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to create object history: " + e.getMessage());
        }
    }

    @Override
    public void handle(DeleteAllObjectsHistoryByFileIdCommand command) {
        var file = externalFileService.fetchFileById(command.fileId());
        if (file == null) {
            throw new IllegalArgumentException("File not found for ID: " + command.fileId());
        }

        try {
            objectHistoryRepository.deleteAllByFile_Id(command.fileId());
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to delete object history: " + e.getMessage());
        }
    }
}
