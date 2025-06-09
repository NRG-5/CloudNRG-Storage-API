package com.cloudnrg.api.iam.application.internal.eventhandlers;

import com.cloudnrg.api.iam.application.internal.outboundservices.acl.ExternalFolderService;
import com.cloudnrg.api.iam.domain.model.events.UserCreationEvent;
import com.cloudnrg.api.iam.domain.model.queries.GetUserByIdQuery;
import com.cloudnrg.api.iam.domain.services.UserQueryService;
import com.cloudnrg.api.storage.domain.model.aggregates.Folder;
import com.cloudnrg.api.storage.infrastructure.persistence.jpa.repositories.FolderRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class UserCreationEventHandler {

    private final UserQueryService userQueryService;
    private final ExternalFolderService externalFolderService;

    public UserCreationEventHandler(
            UserQueryService userQueryService,
            ExternalFolderService externalFolderService
    ) {
        this.userQueryService = userQueryService;
        this.externalFolderService = externalFolderService;
    }

    //on user creation create a root folder for the user
    @EventListener(UserCreationEvent.class)
    public void on(UserCreationEvent event) {

        var user = userQueryService.handle(new GetUserByIdQuery(event.getUserId()));

        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        //create root folder for user
        var rootFolderId = externalFolderService.createRootFolderForUser(user.get().getId());

    }

}
