package com.cloudnrg.api.storage.infrastructure.persistence.jpa.repositories;

import com.cloudnrg.api.storage.domain.model.aggregates.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FolderRepository extends JpaRepository<Folder, UUID> {
    Optional<Folder> findByUser_IdAndName(UUID userId, String name);
}
