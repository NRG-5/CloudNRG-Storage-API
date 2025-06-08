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
        return iamContextFacade.fetchUserById(userId).get();
    }

}
