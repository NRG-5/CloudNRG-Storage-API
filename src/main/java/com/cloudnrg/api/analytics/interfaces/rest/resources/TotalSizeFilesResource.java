package com.cloudnrg.api.analytics.interfaces.rest.resources;

public record TotalSizeFilesResource(
        long totalSize,
        String formattedSize
) {
}
