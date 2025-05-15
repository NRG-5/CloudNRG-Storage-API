package com.cloudnrg.api.storage.domain.services;

import com.cloudnrg.api.storage.domain.model.aggregates.CloudFile;
import com.cloudnrg.api.storage.domain.model.queries.GetFilesByFolderIdQuery;

import java.util.List;

public interface FileQueryService {
    List<CloudFile> handle(GetFilesByFolderIdQuery query);
}
