package com.cloudnrg.api.iam.interfaces.rest.resources;

import java.util.UUID;

public record AuthenticatedUserResource(UUID id, String username, String token) {
}
