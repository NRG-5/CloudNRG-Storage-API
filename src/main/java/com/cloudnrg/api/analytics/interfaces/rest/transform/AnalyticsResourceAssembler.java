package com.cloudnrg.api.analytics.interfaces.rest.transform;

import com.cloudnrg.api.analytics.interfaces.rest.resources.*;

import java.util.Map;

public class AnalyticsResourceAssembler {

    public static TotalSizeFilesResource toTotalFileSizeResource(long totalSize) {
        return new TotalSizeFilesResource(
                totalSize,
                formatFileSize(totalSize)
        );
    }

    public static TotalCountFilesResource toTotalFilesCountResource(long totalFiles) {
        return new TotalCountFilesResource(totalFiles);
    }

    public static FilesByMimeTypeResource toFilesByMimeTypeResource(Map<String, Long> mimeTypes) {
        return new FilesByMimeTypeResource(mimeTypes);
    }

    public static TotalCountFoldersResource toTotalFoldersCountResource(int totalFolders) {
        return new TotalCountFoldersResource(totalFolders);
    }

    private static String formatFileSize(long size) {
        if (size < 1024) {
            return size + " B";
        } else if (size < 1024 * 1024) {
            return String.format("%.2f KB", size / 1024.0);
        } else if (size < 1024 * 1024 * 1024) {
            return String.format("%.2f MB", size / (1024.0 * 1024.0));
        } else {
            return String.format("%.2f GB", size / (1024.0 * 1024.0 * 1024.0));
        }
    }
}