package com.cloudnrg.api.storage.domain.model.commands;

import java.util.UUID;

public record CreateFolderCommand(
        UUID userId,
        String folderName,
        UUID parentFolderId
) {
}
