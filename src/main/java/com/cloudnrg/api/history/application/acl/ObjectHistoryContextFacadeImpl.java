package com.cloudnrg.api.history.application.acl;

import com.cloudnrg.api.history.domain.model.aggregates.ObjectHistory;
import com.cloudnrg.api.history.domain.model.queries.GetAllObjectsHistoryByUserIdQuery;
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
}
