package com.cloudnrg.api.history.application.internal.queryservices;

import com.cloudnrg.api.history.domain.model.aggregates.ObjectHistory;
import com.cloudnrg.api.history.domain.model.queries.*;
import com.cloudnrg.api.history.domain.services.ObjectHistoryQueryService;
import com.cloudnrg.api.history.infrastructure.persistence.jpa.repositories.ObjectHistoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ObjectHistoryQueryServiceImpl implements ObjectHistoryQueryService {
    private final ObjectHistoryRepository repository;

    @Override
    public Optional<ObjectHistory> handle(GetObjectHistoryByIdQuery query) {
        return repository.findById(query.id());
    }

    @Override
    public List<ObjectHistory> handle(GetAllObjectsHistoryByUserIdQuery query) {
        return repository.findAllByUserId(query.userId());
    }

    @Override
    public List<ObjectHistory> handle(GetAllObjectsHistoryByFileIdQuery query) {
        return repository.findAllByFileId(query.fileId());
    }

    @Override
    public List<ObjectHistory> handle(GetLimitedObjectsHistoryByFileIdQuery query) {
        return repository.findByFileIdOrderByCreatedAtDesc(query.fileId(), PageRequest.of(0, query.limit()));
    }

    @Override
    public Optional<ObjectHistory> handle(GetLastObjectHistoryByFileIdQuery query) {
        return repository.findFirstByFileIdOrderByCreatedAtDesc(query.fileId());
    }
}
