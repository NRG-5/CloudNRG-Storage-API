package com.cloudnrg.api.storage.application.internal.queryservices;

import com.cloudnrg.api.storage.domain.model.aggregates.CloudFile;
import com.cloudnrg.api.storage.domain.model.queries.GetFileByIdQuery;
import com.cloudnrg.api.storage.domain.model.queries.GetFilesByFolderIdQuery;
import com.cloudnrg.api.storage.domain.model.queries.GetFilesBySearchQuery;
import com.cloudnrg.api.storage.domain.services.FileQueryService;
import com.cloudnrg.api.storage.infrastructure.persistence.jpa.repositories.CloudFileRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FileQueryServiceImpl implements FileQueryService {

    private final CloudFileRepository cloudFileRepository;

    public FileQueryServiceImpl(CloudFileRepository cloudFileRepository) {
        this.cloudFileRepository = cloudFileRepository;
    }

    @Override
    public List<CloudFile> handle(GetFilesByFolderIdQuery query) {
        return cloudFileRepository.findCloudFilesByFolder_Id(query.folderId());
    }

    @Override
    public Optional<CloudFile> handle(GetFileByIdQuery query) {
        return cloudFileRepository.findById(query.id());
    }

    @Override
    public List<CloudFile> handle(GetFilesBySearchQuery query) {
        return cloudFileRepository.searchCloudFileByFilename(query.searchTerm());
    }

}
