package com.cloudnrg.api.history.application.acl;

import com.cloudnrg.api.history.domain.model.aggregates.ObjectHistory;
import com.cloudnrg.api.history.domain.model.commands.CreateObjectHistoryCommand;
import com.cloudnrg.api.history.domain.model.commands.DeleteAllObjectsHistoryByFileIdCommand;
import com.cloudnrg.api.history.domain.model.events.SaveObjectHistoryCreateFileEvent;
import com.cloudnrg.api.history.domain.model.queries.GetAllObjectsHistoryByFileIdQuery;
import com.cloudnrg.api.history.domain.model.queries.GetAllObjectsHistoryByUserIdQuery;
import com.cloudnrg.api.history.domain.model.queries.GetLastObjectHistoryByFileIdQuery;
import com.cloudnrg.api.history.domain.model.valueobjects.Action;
import com.cloudnrg.api.history.domain.services.ObjectHistoryCommandService;
import com.cloudnrg.api.history.domain.services.ObjectHistoryQueryService;
import com.cloudnrg.api.history.interfaces.acl.ObjectHistoryContextFacade;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ObjectHistoryContextFacadeImpl implements ObjectHistoryContextFacade {
    private final ObjectHistoryCommandService commandService;
    private final ObjectHistoryQueryService queryService;

    public ObjectHistoryContextFacadeImpl(ObjectHistoryCommandService commandService, ObjectHistoryQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @Override
    public List<ObjectHistory> fetchAllObjectsHistoryByUserId(UUID userId) {
        return queryService.handle(new GetAllObjectsHistoryByUserIdQuery(userId));
    }

    @Override
    public List<ObjectHistory> fetchAllObjectsHistoryByFileId(UUID fileId) {
        return queryService.handle(new GetAllObjectsHistoryByFileIdQuery(fileId));
    }

    @Override
    public Optional<ObjectHistory> fetchLastObjectHistoryByFileId(UUID fileId) {
        return queryService.handle(new GetLastObjectHistoryByFileIdQuery(fileId));
    }

    @Override
    public Optional<ObjectHistory> saveObjectHistoryCreateFile(UUID fileId, UUID userId, Action action) {
        var command = new CreateObjectHistoryCommand(fileId, userId, action);
        return commandService.handle(command);
    }

    @Override
    public Optional<ObjectHistory> saveObjectHistoryUpdateFileName(UUID fileId, UUID userId, Action action) {
        var command = new CreateObjectHistoryCommand(fileId, userId, action);
        return commandService.handle(command);
    }

    @Override
    public Optional<ObjectHistory> saveObjectHistoryUpdateFileParentFolder(UUID fileId, UUID userId, Action action) {
        var command = new CreateObjectHistoryCommand(fileId, userId, action);
        return commandService.handle(command);
    }

    @Override
    public void deleteAllObjectsHistoryByFileId(UUID fileId) {
        var command = new DeleteAllObjectsHistoryByFileIdCommand(fileId);
        commandService.handle(command);
    }
}
