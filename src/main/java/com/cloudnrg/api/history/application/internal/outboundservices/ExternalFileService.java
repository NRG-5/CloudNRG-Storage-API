package com.cloudnrg.api.history.application.internal.outboundservices;

import com.cloudnrg.api.storage.domain.model.aggregates.CloudFile;
import com.cloudnrg.api.storage.interfaces.acl.FileContextFacade;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ExternalFileService {
    private final FileContextFacade fileContextFacade;

    public ExternalFileService(FileContextFacade fileContextFacade) {
        this.fileContextFacade = fileContextFacade;
    }

    public CloudFile fetchFileById(UUID fileId) {
        return fileContextFacade.fetchFileById(fileId).get();
    }
}
