package com.cloudnrg.api.storage.application.internal.eventhandlers;

import com.cloudnrg.api.storage.application.internal.outboundservices.acl.ExternalUserService;
import com.cloudnrg.api.storage.domain.model.aggregates.CloudFile;
import com.cloudnrg.api.storage.domain.model.events.CreateFileEvent;
import com.cloudnrg.api.storage.domain.model.queries.GetFileByIdQuery;
import com.cloudnrg.api.storage.domain.model.queries.GetRootFolderByUserIdQuery;
import com.cloudnrg.api.storage.domain.services.FileCommandService;
import com.cloudnrg.api.storage.domain.services.FileQueryService;
import com.cloudnrg.api.storage.domain.services.FolderQueryService;
import com.cloudnrg.api.storage.infrastructure.persistence.jpa.repositories.CloudFileRepository;
import com.cloudnrg.api.storage.infrastructure.persistence.jpa.repositories.FolderRepository;
import com.cloudnrg.api.storage.interfaces.acl.FolderContextFacade;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreateFileEventHandler {
    private final FolderContextFacade folderContextFacade;
    private final ExternalUserService externalUserService;
    private final CloudFileRepository cloudFileRepository;
    private final FolderRepository folderRepository;

    @EventListener(CreateFileEvent.class)
    public void on(CreateFileEvent event) {
        var user = externalUserService.fetchUserById(event.getUserId());

        var getFileByFileIdQuery = cloudFileRepository.findById(event.getFileId());
        var file = getFileByFileIdQuery.get();

        var folder = file.getFolder();

        if (folder.getId() == null) {
            folderContextFacade.createDefaultFolder(user.getId());
        }

        var cloudFile = new CloudFile(
                file.getFilename(),
                folder,
                user,
                file.getSize(),
                file.getMimeType(),
                file.getMd5(),
                file.getPath()
        );

        cloudFileRepository.save(cloudFile);
    }
}

