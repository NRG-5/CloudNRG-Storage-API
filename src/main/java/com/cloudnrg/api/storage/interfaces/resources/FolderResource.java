package com.cloudnrg.api.storage.interfaces.resources;

import java.util.UUID;

public record FolderResource(
        UUID id,
        String name,
        UUID parentFolderId,
        UUID userId
) {
}
