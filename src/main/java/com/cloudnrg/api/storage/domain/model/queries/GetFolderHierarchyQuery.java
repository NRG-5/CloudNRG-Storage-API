package com.cloudnrg.api.storage.domain.model.queries;

import java.util.UUID;

public record GetFolderHierarchyQuery(UUID folderId, UUID userId) {}
