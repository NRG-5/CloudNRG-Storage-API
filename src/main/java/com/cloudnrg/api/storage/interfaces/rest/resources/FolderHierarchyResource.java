package com.cloudnrg.api.storage.interfaces.rest.resources;

import java.util.List;
import java.util.UUID;

public record FolderHierarchyResource(
        UUID id,
        String name,
        UUID parentFolderId,
        UUID userId,
        List<FolderHierarchyResource> parents
) {
}
