package com.cloudnrg.api.storage.application.internal.queryservices;

import com.cloudnrg.api.storage.domain.model.aggregates.Folder;
import com.cloudnrg.api.storage.domain.model.queries.GetFolderAscendantHierarchyQuery;
import com.cloudnrg.api.storage.domain.model.queries.GetFolderByIdQuery;
import com.cloudnrg.api.storage.domain.model.queries.GetFolderDescendantHierarchyQuery;
import com.cloudnrg.api.storage.domain.model.queries.GetRootFolderByUserIdQuery;
import com.cloudnrg.api.storage.domain.services.FolderQueryService;
import com.cloudnrg.api.storage.infrastructure.persistence.jpa.repositories.FolderRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class FolderQueryServiceImpl implements FolderQueryService {

    private final FolderRepository folderRepository;

    public FolderQueryServiceImpl(FolderRepository folderRepository) {
        this.folderRepository = folderRepository;
    }

    @Override
    public Optional<Folder> handle(GetRootFolderByUserIdQuery query) {
        return folderRepository.findFolderByUser_IdAndName(query.userId(), "root");
    }

    @Override
    public Optional<List<Folder>> handle(GetFolderAscendantHierarchyQuery query) {
        var folderResult = folderRepository.findFolderById(query.folderId());
        if (folderResult.isEmpty()) return Optional.empty();
        List<Folder> hierarchy = new ArrayList<>();
        Folder current = folderResult.get();
        while (current != null) {
            hierarchy.add(current);
            current = current.getParentFolder();
        }
        Collections.reverse(hierarchy);
        return Optional.of(hierarchy);
    }

    @Override
    public Optional<Folder> handle(GetFolderDescendantHierarchyQuery query) {
        return folderRepository.findFolderById(query.folderId());
    }

    @Override
    public Optional<Folder> handle(GetFolderByIdQuery query) {
        return folderRepository.findFolderById(query.folderId());
    }
}
