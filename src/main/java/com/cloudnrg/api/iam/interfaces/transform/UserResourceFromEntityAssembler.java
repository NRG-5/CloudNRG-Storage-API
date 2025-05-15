package com.cloudnrg.api.iam.interfaces.transform;

import com.cloudnrg.api.iam.domain.model.aggregates.User;
import com.cloudnrg.api.iam.interfaces.resources.UserResource;

public class UserResourceFromEntityAssembler {

    public static UserResource toResourceFromEntity (User entity) {

        return new UserResource(
                entity.getId(),
                entity.getUsername(),
                entity.getEmail(),
                entity.getPasswordHash()
        );
    }
}
