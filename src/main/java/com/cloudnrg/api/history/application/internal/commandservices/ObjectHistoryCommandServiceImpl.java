package com.cloudnrg.api.history.application.internal.commandservices;

import com.cloudnrg.api.history.domain.model.aggregates.ObjectHistory;
import com.cloudnrg.api.history.domain.model.commands.CreateObjectHistoryCommand;
import com.cloudnrg.api.history.domain.model.commands.DeleteObjectsHistoriesByFileIdCommand;
import com.cloudnrg.api.history.domain.services.ObjectHistoryCommandService;
import com.cloudnrg.api.history.infrastructure.persistence.jpa.repositories.ObjectHistoryRepository;
import com.cloudnrg.api.storage.application.internal.outboundservices.acl.ExternalUserService;
import com.cloudnrg.api.storage.infrastructure.persistence.jpa.repositories.CloudFileRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ObjectHistoryCommandServiceImpl implements ObjectHistoryCommandService {
    private final ObjectHistoryRepository objectHistoryRepository;
    private final ExternalUserService externalUserService;
    private final CloudFileRepository cloudFileRepository;

    public ObjectHistoryCommandServiceImpl(ObjectHistoryRepository objectHistoryRepository,
                                           ExternalUserService externalUserService,
                                            CloudFileRepository cloudFileRepository) {
        this.objectHistoryRepository = objectHistoryRepository;
        this.externalUserService = externalUserService;
        this.cloudFileRepository = cloudFileRepository;
    }

    @Override
    public Optional<ObjectHistory> handle(CreateObjectHistoryCommand command) {
        var file = cloudFileRepository.findCloudFileById(command.fileId());
        var user = externalUserService.fetchUserById(command.userId());


        var objectHistory = new ObjectHistory(
                file,
                user,
                command.action(),
                command.message()
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
    public void handle(DeleteObjectsHistoriesByFileIdCommand command) {
        objectHistoryRepository.deleteAllByFile_Id(command.fileId());
    }
}
