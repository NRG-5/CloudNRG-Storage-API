package com.cloudnrg.api.storage.interfaces.acl;

import com.cloudnrg.api.storage.domain.model.aggregates.Folder;

import java.util.List;
import java.util.UUID;

public interface FolderContextFacade {

    UUID createDefaultFolder(UUID userId);

    List<Folder> fetchAllFoldersByUserId(UUID userId);
}
