package com.cloudnrg.api.storage.interfaces.acl;

import com.cloudnrg.api.storage.domain.model.aggregates.CloudFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FileContextFacade {
    UUID createFile(MultipartFile file, UUID userId, UUID folderId);
    void deleteFileById(UUID fileId);
    Optional<CloudFile> fetchFileById(UUID fileId);
    List<CloudFile> fetchFilesByFolderId(UUID folderId);
    List<CloudFile> fetchAllFilesByUserId(UUID userId);
}
