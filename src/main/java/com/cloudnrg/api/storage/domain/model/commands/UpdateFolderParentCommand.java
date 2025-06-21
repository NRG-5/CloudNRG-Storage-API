package com.cloudnrg.api.storage.domain.model.commands;

import java.util.UUID;

public record UpdateFolderParentCommand(UUID folderId, UUID parentId, UUID userId) {}
