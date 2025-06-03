package com.cloudnrg.api.auditlog.domain.model.events;

import java.util.UUID;

public class UserUploadedFileEvent {
    private final UUID userId;
    private final UUID fileId;
    private final String fileName;

    public UserUploadedFileEvent(UUID userId, UUID fileId, String fileName) {
        this.userId = userId;
        this.fileId = fileId;
        this.fileName = fileName;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getFileId() {
        return fileId;
    }

    public String getFileName() {
        return fileName;
    }
}