package com.cloudnrg.api.storage.application.internal.outboundservices.acl;

import com.cloudnrg.api.iam.domain.model.aggregates.User;
import com.cloudnrg.api.iam.interfaces.acl.IamContextFacade;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ExternalUserService {

    private final IamContextFacade iamContextFacade;

    public ExternalUserService(IamContextFacade iamContextFacade) {
        this.iamContextFacade = iamContextFacade;
    }

    public User fetchUserById(UUID userId) {
        var user = iamContextFacade.fetchUserById(userId);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("User with ID " + userId + " does not exist.");
        }
        return user.get();
    }

    public UUID fetchUserByUsername(String username)  {
        return iamContextFacade.fetchUserIdByUsername(username);
    }
}
