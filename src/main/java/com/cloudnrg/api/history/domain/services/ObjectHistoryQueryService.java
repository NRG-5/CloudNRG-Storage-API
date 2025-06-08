package com.cloudnrg.api.history.domain.services;

import com.cloudnrg.api.history.domain.model.aggregates.ObjectHistory;
import com.cloudnrg.api.history.domain.model.queries.*;

import java.util.List;
import java.util.Optional;

public interface ObjectHistoryQueryService {
    Optional<ObjectHistory> handle(GetObjectHistoryByIdQuery query);
    List<ObjectHistory> handle(GetAllObjectsHistoryByUserIdQuery query);
    List<ObjectHistory> handle(GetAllObjectsHistoryByFileIdQuery query);
    List<ObjectHistory> handle(GetLimitedObjectsHistoryByFileIdQuery query);
    Optional<ObjectHistory> handle(GetLastObjectHistoryByFileIdQuery query);
}
