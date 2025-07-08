package com.cloudnrg.api.storage.domain.services;

import com.cloudnrg.api.storage.domain.model.aggregates.Folder;
import com.cloudnrg.api.storage.domain.model.queries.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FolderQueryService {
    Optional<Folder> handle(GetRootFolderByUserIdQuery query);
    Optional<List<Folder>> handle(GetFolderAscendantHierarchyQuery query);
    Optional<Folder> handle(GetFolderDescendantHierarchyQuery query);
    Optional<List<Folder>> handle(GetAllFoldersByUserIdQuery query);
    Optional<Folder> handle(GetFolderByIdQuery query);

    List<Folder> handle(GetFoldersByParentFolderIdQuery query);
    Optional<List<Folder>> handle(GetFolderHierarchyQuery query);
    List<Folder> searchByName(String query, UUID userId);
}
