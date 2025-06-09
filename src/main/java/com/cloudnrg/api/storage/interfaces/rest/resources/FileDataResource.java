package com.cloudnrg.api.storage.interfaces.rest.resources;

import java.util.UUID;

public record FileDataResource(
        Long createTime,
        String downloadUrl,
        UUID fileId,
        String md5,
        String mimeType,
        Long modTime,
        String name,
        String parentFolder,
        Long size,
        String type
) {
}
