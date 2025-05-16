package com.cloudnrg.api.iam.domain.model.commands;

public record CreateUserCommand(
        String username,
        String email,
        String passwordHash
) {
}
