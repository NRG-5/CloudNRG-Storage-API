package com.cloudnrg.api.history.interfaces.acl;

import java.util.UUID;

public interface ObjectHistoryContextFacade {

    UUID createObjectHistory(
            UUID fileId,
            UUID userId,
            String action,
            String message
    );

    void deleteObjectHistoriesByFileId(UUID fileId);
}
