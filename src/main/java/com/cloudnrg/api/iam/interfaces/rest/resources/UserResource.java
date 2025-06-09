package com.cloudnrg.api.iam.interfaces.rest.resources;

import java.util.UUID;

public record UserResource(
        UUID id,
        String username,
        String email,
        String passwordHash
) {
}
