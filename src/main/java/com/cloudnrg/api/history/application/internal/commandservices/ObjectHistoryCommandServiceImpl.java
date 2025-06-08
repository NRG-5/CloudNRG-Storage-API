package com.cloudnrg.api.history.application.internal.commandservices;

import com.cloudnrg.api.history.domain.model.aggregates.ObjectHistory;
import com.cloudnrg.api.history.domain.model.commands.CreateObjectHistoryCommand;
import com.cloudnrg.api.history.domain.model.commands.DeleteAllObjectsHistoryByFileIdCommand;
import com.cloudnrg.api.history.domain.services.ObjectHistoryCommandService;
import com.cloudnrg.api.history.infrastructure.persistence.jpa.repositories.ObjectHistoryRepository;
import com.cloudnrg.api.iam.infrastructure.persistance.jpa.repositories.UserRepository;
import com.cloudnrg.api.storage.infrastructure.persistence.jpa.repositories.CloudFileRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ObjectHistoryCommandServiceImpl implements ObjectHistoryCommandService {
    private final ObjectHistoryRepository objectHistoryRepository;
    private final UserRepository userRepository;
    private final CloudFileRepository cloudFileRepository;

    @Override
    public Optional<ObjectHistory> handle(CreateObjectHistoryCommand command) {
        var file = cloudFileRepository.findById(command.fileId());
        var user = userRepository.findUserById(command.userId());

        if (file.isEmpty()) {
            throw new RuntimeException("File not found");
        }
        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        var objectHistory = new ObjectHistory(
                file.get(),
                user.get(),
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
            objectHistoryRepository.deleteAllByFileId(command.fileId());
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to delete object history: " + e.getMessage());
        }
    }
}
