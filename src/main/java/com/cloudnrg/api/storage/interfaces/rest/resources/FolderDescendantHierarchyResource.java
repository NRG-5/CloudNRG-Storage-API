package com.cloudnrg.api.storage.interfaces.rest.resources;

import java.util.List;
import java.util.UUID;

public record FolderDescendantHierarchyResource(
        UUID id,
        String name,
        UUID parentFolderId,
        UUID userId,
        List<FolderDescendantHierarchyResource> children
) {}
