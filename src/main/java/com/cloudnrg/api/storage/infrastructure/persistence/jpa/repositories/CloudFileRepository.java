package com.cloudnrg.api.storage.infrastructure.persistence.jpa.repositories;

import com.cloudnrg.api.storage.domain.model.aggregates.CloudFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CloudFileRepository extends JpaRepository<CloudFile, UUID> {

}
