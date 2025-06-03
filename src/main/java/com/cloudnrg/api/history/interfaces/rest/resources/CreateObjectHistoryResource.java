package com.cloudnrg.api.history.interfaces.rest.resources;

import java.util.UUID;

public record CreateObjectHistoryResource(UUID fileId, UUID userId, String action) {
}
