package com.cloudnrg.api.storage.interfaces.rest.resources;

import java.util.List;
import java.util.UUID;

public record FolderAscendantHierarchyResource(
        UUID id,
        String name,
        UUID parentFolderId,
        UUID userId,
        List<FolderAscendantHierarchyResource> parents
) {
}
