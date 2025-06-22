package com.cloudnrg.api.storage.application.internal.outboundservices.acl;

import com.cloudnrg.api.history.domain.model.aggregates.ObjectHistory;
import com.cloudnrg.api.history.interfaces.acl.ObjectHistoryContextFacade;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ExternalObjectHistoryService {
    private final ObjectHistoryContextFacade objectHistoryContextFacade;

    public ExternalObjectHistoryService(@Lazy ObjectHistoryContextFacade objectHistoryContextFacade) {
        this.objectHistoryContextFacade = objectHistoryContextFacade;
    }

    public Optional<ObjectHistory> saveObjectHistoryCreateFile(UUID fileId, UUID userId) {
        return objectHistoryContextFacade.saveObjectHistoryCreateFile(fileId, userId);
    }

    public Optional<ObjectHistory> saveObjectHistoryUpdateFileName(UUID fileId, UUID userId, String newFileName) {
        return objectHistoryContextFacade.saveObjectHistoryUpdateFileName(fileId, userId, newFileName);
    }

    public Optional<ObjectHistory> saveObjectHistoryUpdateFileParentFolder(UUID fileId, UUID userId, UUID newParentFolderId) {
        return objectHistoryContextFacade.saveObjectHistoryUpdateFileParentFolder(fileId, userId, newParentFolderId);
    }

    public void deleteAllObjectsHistoryByFileId(UUID fileId) {
        objectHistoryContextFacade.deleteAllObjectsHistoryByFileId(fileId);
    }
}
