package com.cloudnrg.api.storage.domain.services;

import com.cloudnrg.api.storage.domain.model.aggregates.Folder;
import com.cloudnrg.api.storage.domain.model.queries.*;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface FolderQueryService {
    Optional<Folder> handle(GetRootFolderByUserIdQuery query);
    Optional<List<Folder>> handle(GetFolderAscendantHierarchyQuery query);
    Optional<Folder> handle(GetFolderDescendantHierarchyQuery query);

    Optional<Folder> handle(GetFolderByIdQuery query);

    List<Folder> handle(GetFoldersByParentFolderIdQuery query);
}
