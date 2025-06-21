package com.cloudnrg.api.storage.domain.model.commands;

import java.util.UUID;

public record DeleteFolderByIdCommand(UUID folderId, UUID userId) {}
