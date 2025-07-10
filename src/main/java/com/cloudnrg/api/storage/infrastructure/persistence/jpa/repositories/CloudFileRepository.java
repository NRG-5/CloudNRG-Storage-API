package com.cloudnrg.api.storage.infrastructure.persistence.jpa.repositories;

import com.cloudnrg.api.storage.domain.model.aggregates.CloudFile;
import com.cloudnrg.api.storage.domain.model.valueobjects.MimeTypeCount;
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

    Long countCloudFileByUser_Id(UUID userId);

    @Query(value = "SELECT COALESCE(SUM(size), 0) FROM cloud_files WHERE user_id = CAST(:userId AS uuid)", nativeQuery = true)
    Long sumAllFileSizes(UUID userId);

    @Query(value = "SELECT COUNT(filename) AS count, mime_type AS mimeType " +
            "FROM cloud_files " +
            "WHERE user_id = CAST(:userId AS uuid) " +
            "GROUP BY mime_type", nativeQuery = true)
    List<MimeTypeCount> countByUserIdGroupedByMimeType(@Param("userId") String userId);







}
