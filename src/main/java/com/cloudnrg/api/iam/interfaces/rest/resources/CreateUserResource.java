package com.cloudnrg.api.iam.interfaces.rest.resources;

public record CreateUserResource(
        String username,
        String email,
        String passwordHash
) {
}
