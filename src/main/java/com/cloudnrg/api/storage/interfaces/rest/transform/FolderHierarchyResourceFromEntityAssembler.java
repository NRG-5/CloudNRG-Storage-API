package com.cloudnrg.api.storage.interfaces.rest.transform;

import com.cloudnrg.api.storage.domain.model.aggregates.Folder;
import com.cloudnrg.api.storage.interfaces.rest.resources.FolderHierarchyResource;

import java.util.List;

public class FolderHierarchyResourceFromEntityAssembler {
    public static FolderHierarchyResource toResourceFromHierarchy(List<Folder> hierarchy) {
        FolderHierarchyResource resource = null;
        for (int i = hierarchy.size() - 1; i >= 0; i--) {
            Folder f = hierarchy.get(i);
            resource = new FolderHierarchyResource(
                    f.getId(),
                    f.getName(),
                    f.getParentFolder() != null ? f.getParentFolder().getId() : null,
                    f.getUser().getId(),
                    resource == null ? List.of() : List.of(resource)
            );
        }
        return resource;
    }
}
