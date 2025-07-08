package com.cloudnrg.api.storage.application.internal.queryservices;

import com.cloudnrg.api.storage.domain.model.aggregates.Folder;
import com.cloudnrg.api.storage.domain.model.queries.*;
import com.cloudnrg.api.storage.domain.services.FolderQueryService;
import com.cloudnrg.api.storage.infrastructure.persistence.jpa.repositories.FolderRepository;
import org.springframework.stereotype.Service;

import java.util.*;

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

    public List<Folder> searchByName(String query, UUID userId) {
        return folderRepository.findByNameContainingIgnoreCaseAndUser_Id(query, userId);
    }

    @Override
    public Optional<List<Folder>> handle(GetFolderHierarchyQuery query) {
        var folderOpt = folderRepository.findFolderById(query.folderId());
        if (folderOpt.isEmpty()) return Optional.empty();
        List<Folder> hierarchy = new ArrayList<>();
        Folder current = folderOpt.get();
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
    public Optional<List<Folder>> handle(GetAllFoldersByUserIdQuery query) {
        return folderRepository.findAllFoldersByUser_Id(query.userId());
    }

    @Override
    public Optional<Folder> handle(GetFolderByIdQuery query) {
        return folderRepository.findFolderById(query.folderId());
    }

    @Override
    public List<Folder> handle(GetFoldersByParentFolderIdQuery query) {
        return folderRepository.findFoldersByParentFolder_Id(query.parentFolderId());
    }
}
