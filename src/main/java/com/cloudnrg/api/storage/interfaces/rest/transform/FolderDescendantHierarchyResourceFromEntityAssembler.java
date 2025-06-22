package com.cloudnrg.api.storage.interfaces.rest.transform;

import com.cloudnrg.api.storage.domain.model.aggregates.Folder;
import com.cloudnrg.api.storage.infrastructure.persistence.jpa.repositories.FolderRepository;
import com.cloudnrg.api.storage.interfaces.rest.resources.FolderDescendantHierarchyResource;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class FolderDescendantHierarchyResourceFromEntityAssembler {
    private final FolderRepository folderRepository;

    public FolderDescendantHierarchyResource toResourceFromEntity(Folder entity) {
        List<FolderDescendantHierarchyResource> childrenResources = folderRepository
                .findAllByParentFolder_Id(entity.getId())
                .orElse(List.of())
                .stream()
                .map(this::toResourceFromEntity)
                .collect(Collectors.toList());

        return new FolderDescendantHierarchyResource(
                entity.getId(),
                entity.getName(),
                entity.getParentFolder() != null ? entity.getParentFolder().getId() : null,
                entity.getUser().getId(),
                childrenResources
        );
    }
}
