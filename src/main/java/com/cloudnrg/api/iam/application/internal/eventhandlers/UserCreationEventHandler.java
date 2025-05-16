package com.cloudnrg.api.iam.application.internal.eventhandlers;

import com.cloudnrg.api.iam.domain.model.events.UserCreationEvent;
import com.cloudnrg.api.iam.domain.model.queries.GetUserByIdQuery;
import com.cloudnrg.api.iam.domain.services.UserQueryService;
import com.cloudnrg.api.storage.domain.model.aggregates.Folder;
import com.cloudnrg.api.storage.infrastructure.persistence.jpa.repositories.FolderRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class UserCreationEventHandler {

    private final FolderRepository folderRepository;
    private final UserQueryService userQueryService;

    public UserCreationEventHandler(
            FolderRepository folderRepository,
            UserQueryService userQueryService) {
        this.folderRepository = folderRepository;
        this.userQueryService = userQueryService;
    }

    //on user creation create a root folder for the user
    //TODO: refactor folder creation as an outbound service
    @EventListener(UserCreationEvent.class)
    public void on(UserCreationEvent event) {

        var user = userQueryService.handle(new GetUserByIdQuery(event.getUserId()));

        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        var defaultFolder = new Folder(
                "root",
                null,
                user.get()
        );

        try {
            folderRepository.save(defaultFolder);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create root folder: " + e.getMessage());
        }

    }

}
