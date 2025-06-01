package com.cloudnrg.api.history.domain.services;

import com.cloudnrg.api.history.domain.model.aggregates.ObjectHistory;
import com.cloudnrg.api.history.domain.model.queries.GetAllObjectsHistoryByFileIdQuery;
import com.cloudnrg.api.history.domain.model.queries.GetAllObjectsHistoryByUserIdQuery;
import com.cloudnrg.api.history.domain.model.queries.GetObjectHistoryByIdQuery;

import java.util.List;
import java.util.Optional;

public interface ObjectHistoryQueryService {
    Optional<ObjectHistory> handle(GetObjectHistoryByIdQuery query);
    List<ObjectHistory> handle(GetAllObjectsHistoryByUserIdQuery query);
    List<ObjectHistory> handle(GetAllObjectsHistoryByFileIdQuery query);
}
