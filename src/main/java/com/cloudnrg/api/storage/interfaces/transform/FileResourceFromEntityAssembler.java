package com.cloudnrg.api.storage.interfaces.transform;

import com.cloudnrg.api.storage.domain.model.aggregates.CloudFile;
import com.cloudnrg.api.storage.interfaces.resources.FileDataResource;
import com.cloudnrg.api.storage.interfaces.resources.FileResource;

public class FileResourceFromEntityAssembler {
    public static FileResource toResourceFromEntity(CloudFile entity, String status) {
        return new FileResource(
                new FileDataResource(
                        entity.getCreatedAt().getEpochSecond(),
                        "",
                        entity.getId(),
                        entity.getMd5(),
                        entity.getMimeType(),
                        entity.getUpdatedAt().getEpochSecond(),
                        entity.getFilename(),
                        entity.getFolder().getName(),
                        entity.getSize(),
                        "file"
                ),
                status
        );
    }
}
