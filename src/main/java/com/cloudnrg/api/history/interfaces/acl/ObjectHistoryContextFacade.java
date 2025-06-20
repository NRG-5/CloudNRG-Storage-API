package com.cloudnrg.api.history.interfaces.acl;

import com.cloudnrg.api.history.domain.model.aggregates.ObjectHistory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ObjectHistoryContextFacade {
    List<ObjectHistory> fetchAllObjectsHistoryByUserId(UUID userId);
    List<ObjectHistory> fetchAllObjectsHistoryByFileId(UUID fileId);

    Optional<ObjectHistory> saveObjectHistoryCreateFile(UUID fileId, UUID userId);
    Optional<ObjectHistory> saveObjectHistoryUpdateFileName(UUID fileId, UUID userId, String newFileName);
    Optional<ObjectHistory> saveObjectHistoryUpdateFileParentFolder(UUID fileId, UUID userId, UUID newParentFolderId);

    void deleteAllObjectsHistoryByFileId(UUID fileId);
}
