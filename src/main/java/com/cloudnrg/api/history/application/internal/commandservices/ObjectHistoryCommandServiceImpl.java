package com.cloudnrg.api.history.application.internal.commandservices;

import com.cloudnrg.api.history.application.internal.outboundservices.ExternalFileService;
import com.cloudnrg.api.history.domain.model.aggregates.ObjectHistory;
import com.cloudnrg.api.history.domain.model.commands.CreateObjectHistoryCommand;
import com.cloudnrg.api.history.domain.model.commands.DeleteAllObjectsHistoryByFileIdCommand;
import com.cloudnrg.api.history.domain.services.ObjectHistoryCommandService;
import com.cloudnrg.api.history.infrastructure.persistence.jpa.repositories.ObjectHistoryRepository;
import com.cloudnrg.api.iam.infrastructure.persistance.jpa.repositories.UserRepository;
import com.cloudnrg.api.storage.application.internal.outboundservices.acl.ExternalUserService;
import com.cloudnrg.api.storage.infrastructure.persistence.jpa.repositories.CloudFileRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ObjectHistoryCommandServiceImpl implements ObjectHistoryCommandService {
    private final ObjectHistoryRepository objectHistoryRepository;
    private final ExternalUserService externalUserService;
    private final ExternalFileService externalFileService;
    private final ApplicationEventPublisher applicationEventPublisher;

    public ObjectHistoryCommandServiceImpl(ObjectHistoryRepository objectHistoryRepository, ExternalUserService externalUserService, ExternalFileService externalFileService, ApplicationEventPublisher applicationEventPublisher) {
        this.objectHistoryRepository = objectHistoryRepository;
        this.externalUserService = externalUserService;
        this.externalFileService = externalFileService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public Optional<ObjectHistory> handle(CreateObjectHistoryCommand command) {
        var file = externalFileService.fetchFileById(command.fileId());
        var user = externalUserService.fetchUserById(command.userId());

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
    @Transactional
    public void handle(DeleteAllObjectsHistoryByFileIdCommand command) {
        var file = externalFileService.fetchFileById(command.fileId());

        if (file == null) {
            throw new IllegalArgumentException("File with ID " + command.fileId() + " does not exist.");
        }

        try {
            objectHistoryRepository.deleteAllByFile_Id(command.fileId());
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to delete object history for file ID: " + command.fileId() + ", Error: " + e.getMessage());
        }
    }
}
