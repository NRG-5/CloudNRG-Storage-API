package com.cloudnrg.api.storage.interfaces.rest.transform;

import com.cloudnrg.api.storage.domain.model.aggregates.Folder;
import com.cloudnrg.api.storage.interfaces.rest.resources.FolderAscendantHierarchyResource;

import java.util.List;

public class FolderAscendantHierarchyResourceFromEntityAssembler {
    public static FolderAscendantHierarchyResource toResourceFromHierarchy(List<Folder> hierarchy) {
        FolderAscendantHierarchyResource resource = null;
        for (int i = hierarchy.size() - 1; i >= 0; i--) {
            Folder f = hierarchy.get(i);
            resource = new FolderAscendantHierarchyResource(
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
