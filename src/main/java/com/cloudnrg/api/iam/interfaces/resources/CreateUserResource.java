package com.cloudnrg.api.iam.interfaces.resources;

public record CreateUserResource(
        String username,
        String email,
        String passwordHash
) {
}
