package com.cloudnrg.api.history.application.acl;

import com.cloudnrg.api.history.domain.model.aggregates.ObjectHistory;
import com.cloudnrg.api.history.domain.model.commands.CreateObjectHistoryCommand;
import com.cloudnrg.api.history.domain.model.commands.DeleteAllObjectsHistoryByFileIdCommand;
import com.cloudnrg.api.history.domain.model.queries.GetAllObjectsHistoryByFileIdQuery;
import com.cloudnrg.api.history.domain.model.queries.GetAllObjectsHistoryByUserIdQuery;
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
@AllArgsConstructor
public class ObjectHistoryContextFacadeImpl implements ObjectHistoryContextFacade {
    private final ObjectHistoryCommandService commandService;
    private final ObjectHistoryQueryService queryService;

    @Override
    public List<ObjectHistory> fetchAllObjectsHistoryByUserId(UUID userId) {
        return queryService.handle(new GetAllObjectsHistoryByUserIdQuery(userId));
    }

    @Override
    public List<ObjectHistory> fetchAllObjectsHistoryByFileId(UUID fileId) {
        return queryService.handle(new GetAllObjectsHistoryByFileIdQuery(fileId));
    }

    @Override
    public Optional<ObjectHistory> saveObjectHistoryCreateFile(UUID fileId, UUID userId) {
        return commandService.handle(new CreateObjectHistoryCommand(fileId, userId, Action.CREATE_FILE));
    }

    @Override
    public Optional<ObjectHistory> saveObjectHistoryUpdateFileName(UUID fileId, UUID userId, String newFileName) {
        return commandService.handle(new CreateObjectHistoryCommand(fileId, userId, Action.UPDATE_FILE_NAME));
    }

    @Override
    public Optional<ObjectHistory> saveObjectHistoryUpdateFileParentFolder(UUID fileId, UUID userId, UUID newParentFolderId) {
        return commandService.handle(new CreateObjectHistoryCommand(fileId, userId, Action.UPDATE_FILE_PARENT_FOLDER));
    }

    @Override
    public void deleteAllObjectsHistoryByFileId(UUID fileId) {
        commandService.handle(new DeleteAllObjectsHistoryByFileIdCommand(fileId));
    }
}
