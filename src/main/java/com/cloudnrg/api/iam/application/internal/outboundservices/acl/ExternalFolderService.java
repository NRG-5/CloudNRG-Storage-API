package com.cloudnrg.api.iam.application.internal.outboundservices.acl;

import com.cloudnrg.api.storage.interfaces.acl.FolderContextFacade;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ExternalFolderService {

    private final FolderContextFacade folderContextFacade;

    public ExternalFolderService(FolderContextFacade folderContextFacade) {
        this.folderContextFacade = folderContextFacade;
    }

    public UUID createRootFolderForUser(UUID userId) {
        return folderContextFacade.createDefaultFolder(userId);
    }

}
