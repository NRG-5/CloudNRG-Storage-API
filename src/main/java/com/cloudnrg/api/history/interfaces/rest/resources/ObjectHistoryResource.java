package com.cloudnrg.api.history.interfaces.rest.resources;

import java.util.UUID;

public record ObjectHistoryResource(UUID id, UUID fileId, UUID userId, String action) {
}
