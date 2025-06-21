package com.cloudnrg.api.storage.domain.model.commands;

import java.util.UUID;

public record UpdateFolderNameCommand(UUID folderId, String name, UUID userId) {}