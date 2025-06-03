package com.cloudnrg.api.storage.interfaces.rest.transform;

import com.cloudnrg.api.storage.domain.model.aggregates.Folder;
import com.cloudnrg.api.storage.interfaces.rest.resources.FolderResource;

public class FolderResourceFromEntityAssembler {
    public static FolderResource toResourceFromEntity (Folder entity){
        return new FolderResource(
                entity.getId(),
                entity.getName(),
                entity.getParentFolder() != null ? entity.getParentFolder().getId() : null,
                entity.getUser().getId(),
                entity.getCreatedAt().getEpochSecond(),
                entity.getUpdatedAt().getEpochSecond()
        );
    }
}
