package com.cloudnrg.api.storage.domain.services;

import com.cloudnrg.api.storage.domain.model.aggregates.CloudFile;
import com.cloudnrg.api.storage.domain.model.queries.GetFileByIdQuery;
import com.cloudnrg.api.storage.domain.model.queries.GetFilesByFolderIdQuery;
import com.cloudnrg.api.storage.domain.model.queries.GetFilesBySearchQuery;

import java.util.List;
import java.util.Optional;

public interface FileQueryService {
    List<CloudFile> handle(GetFilesByFolderIdQuery query);
    Optional<CloudFile> handle(GetFileByIdQuery query);

    List<CloudFile> handle(GetFilesBySearchQuery query);
}
