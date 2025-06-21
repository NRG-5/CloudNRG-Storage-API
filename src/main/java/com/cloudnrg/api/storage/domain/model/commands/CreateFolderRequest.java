package com.cloudnrg.api.storage.domain.model.commands;

import java.util.UUID;

public record CreateFolderRequest(String folderName, UUID parentFolderId) {}
