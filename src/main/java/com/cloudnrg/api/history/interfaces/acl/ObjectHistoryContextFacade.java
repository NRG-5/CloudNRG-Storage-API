package com.cloudnrg.api.history.interfaces.acl;

import com.cloudnrg.api.history.domain.model.aggregates.ObjectHistory;

import java.util.List;
import java.util.UUID;

public interface ObjectHistoryContextFacade {
    List<ObjectHistory> fetchAllObjectsHistoryByUserId(UUID userId);
}
