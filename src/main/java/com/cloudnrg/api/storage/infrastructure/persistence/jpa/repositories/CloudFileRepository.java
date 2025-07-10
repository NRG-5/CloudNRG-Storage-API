package com.cloudnrg.api.storage.infrastructure.persistence.jpa.repositories;

import com.cloudnrg.api.storage.domain.model.aggregates.CloudFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CloudFileRepository extends JpaRepository<CloudFile, UUID> {

    List<CloudFile> findCloudFilesByFolder_Id(UUID folderId);

    CloudFile findCloudFileById(UUID id);

    @Query(
            value =
                    "SELECT * FROM cloud_files " +
                            "WHERE to_tsvector('english', filename) @@ websearch_to_tsquery('english', :query || '*') " +
                            "   OR filename ILIKE '%' || :query || '%' " +
                            "ORDER BY " +
                            "  (to_tsvector('english', filename) @@ websearch_to_tsquery('english', :query || '*')) DESC, " +
                            "  ts_rank(" +
                            "    to_tsvector('english', filename)," +
                            "    websearch_to_tsquery('english', :query || '*')" +
                            "  ) DESC, " +
                            "  filename ASC " +
                            "LIMIT 20",
            nativeQuery = true
    )
    List<CloudFile> searchCloudFileByFilename(@Param("query") String query);

    void deleteCloudFilesByFolder_Id(UUID folderId);
}
