package com.cloudnrg.api.storage.domain.model.commands;

import java.util.UUID;

public record UpdateFileFolderCommand(UUID fileId, UUID folderId) {

}
