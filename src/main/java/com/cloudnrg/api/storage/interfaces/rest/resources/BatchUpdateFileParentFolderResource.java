package com.cloudnrg.api.storage.interfaces.rest.resources;

import java.util.List;
import java.util.UUID;

public record BatchUpdateFileParentFolderResource(
        List<UUID> fileIds,
        UUID newParentFolderId
) {
}
