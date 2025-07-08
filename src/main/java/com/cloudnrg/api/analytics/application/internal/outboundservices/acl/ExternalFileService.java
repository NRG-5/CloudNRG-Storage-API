package com.cloudnrg.api.analytics.application.internal.outboundservices.acl;

import com.cloudnrg.api.storage.domain.model.aggregates.CloudFile;
import com.cloudnrg.api.storage.interfaces.acl.FileContextFacade;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ExternalFileService {
    private final FileContextFacade fileContextFacade;

    public ExternalFileService(FileContextFacade fileContextFacade) {
        this.fileContextFacade = fileContextFacade;
    }

    public List<CloudFile> fetchAllFilesByUserId(UUID userId) {
        var files = fileContextFacade.fetchAllFilesByUserId(userId);
        if (files.isEmpty()) {
            throw new IllegalArgumentException("No files found for user with ID " + userId);
        }
        return files;
    }
}
