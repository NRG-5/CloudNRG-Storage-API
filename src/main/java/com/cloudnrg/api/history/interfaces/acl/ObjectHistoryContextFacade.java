package com.cloudnrg.api.history.interfaces.acl;

import com.cloudnrg.api.history.domain.model.aggregates.ObjectHistory;
import com.cloudnrg.api.history.domain.model.valueobjects.Action;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ObjectHistoryContextFacade {
    List<ObjectHistory> fetchAllObjectsHistoryByUserId(UUID userId);
    List<ObjectHistory> fetchAllObjectsHistoryByFileId(UUID fileId);
    Optional<ObjectHistory> fetchLastObjectHistoryByFileId(UUID fileId);

    Optional<ObjectHistory> saveObjectHistoryCreateFile(UUID fileId, UUID userId, Action action);
    Optional<ObjectHistory> saveObjectHistoryUpdateFileName(UUID fileId, UUID userId, Action action);
    Optional<ObjectHistory> saveObjectHistoryUpdateFileParentFolder(UUID fileId, UUID userId, Action action);

    void deleteAllObjectsHistoryByFileId(UUID fileId);
}
