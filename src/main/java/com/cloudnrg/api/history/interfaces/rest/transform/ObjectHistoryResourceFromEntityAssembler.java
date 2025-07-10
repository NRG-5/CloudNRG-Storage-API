package com.cloudnrg.api.history.interfaces.rest.transform;

import com.cloudnrg.api.history.domain.model.aggregates.ObjectHistory;
import com.cloudnrg.api.history.interfaces.rest.resources.ObjectHistoryResource;

public class ObjectHistoryResourceFromEntityAssembler {
    public static ObjectHistoryResource toResourceFromEntity(ObjectHistory entity) {
        return new ObjectHistoryResource(
                entity.getId(),
                entity.getFileId(),
                entity.getUserId(),
                entity.getAction(),
                entity.getMessage()
        );
    }
}
