package com.cloudnrg.api.history.application.internal.commandservices;

import com.cloudnrg.api.history.domain.model.aggregates.ObjectHistory;
import com.cloudnrg.api.history.domain.model.commands.CreateObjectHistoryCommand;
import com.cloudnrg.api.history.domain.model.commands.DeleteAllObjectsHistoryByFileIdCommand;
import com.cloudnrg.api.history.domain.services.ObjectHistoryCommandService;
import com.cloudnrg.api.history.infrastructure.persistence.jpa.repositories.ObjectHistoryRepository;
import com.cloudnrg.api.iam.infrastructure.persistance.jpa.repositories.UserRepository;
import com.cloudnrg.api.storage.application.internal.outboundservices.acl.ExternalUserService;
import com.cloudnrg.api.storage.infrastructure.persistence.jpa.repositories.CloudFileRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ObjectHistoryCommandServiceImpl implements ObjectHistoryCommandService {
    private final ObjectHistoryRepository objectHistoryRepository;
    private final ExternalUserService externalUserService;

    //TODO: change to external user service
    private final UserRepository userRepository;

    //TODO: change to external file service
    private final CloudFileRepository cloudFileRepository;

    public ObjectHistoryCommandServiceImpl(ObjectHistoryRepository objectHistoryRepository, UserRepository userRepository, CloudFileRepository cloudFileRepository) {
        this.objectHistoryRepository = objectHistoryRepository;
        this.userRepository = userRepository;
        this.cloudFileRepository = cloudFileRepository;
    }

    @Override
    public Optional<ObjectHistory> handle(CreateObjectHistoryCommand command) {
        var file = cloudFileRepository.findById(command.fileId());
        var user = externalUserService.fetchUserById(command.userId());

        if (file.isEmpty()) {
            throw new RuntimeException("File not found");
        }

        var objectHistory = new ObjectHistory(
                file.get(),
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
        var file = cloudFileRepository.findById(command.fileId());
        if (file.isEmpty()) {
            throw new RuntimeException("File not found");
        }

        try {
            objectHistoryRepository.deleteAllByFile_Id(command.fileId());
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to delete object history: " + e.getMessage());
        }
    }
}
