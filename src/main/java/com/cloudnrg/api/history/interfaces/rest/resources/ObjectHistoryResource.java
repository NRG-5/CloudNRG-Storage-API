package com.cloudnrg.api.history.interfaces.rest.resources;

import com.cloudnrg.api.history.domain.model.valueobjects.Action;

import java.util.UUID;

public record ObjectHistoryResource(UUID id, UUID fileId, UUID userId, Action action, String message) {
}
