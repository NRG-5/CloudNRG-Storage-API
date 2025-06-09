package com.cloudnrg.api.storage.interfaces.rest.resources;

import java.util.UUID;

public record FolderResource(
        UUID id,
        String name,
        UUID parentFolderId,
        UUID userId,
        Long createTime,
        Long modTime
) {
}
