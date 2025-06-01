package com.cloudnrg.api.history.infrastructure.persistence.jpa.repositories;

import com.cloudnrg.api.history.domain.model.aggregates.ObjectHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ObjectHistoryRepository extends JpaRepository<ObjectHistory, Long> {
    Optional<ObjectHistory> findById(UUID id);
    List<ObjectHistory> findAllByUserId(UUID userId);
    List<ObjectHistory> findAllByFileId(UUID fileId);
}
