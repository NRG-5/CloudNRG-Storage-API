package com.cloudnrg.api.history.interfaces.rest.resources;

import com.cloudnrg.api.history.domain.model.valueobjects.Action;

import java.util.UUID;

public record CreateObjectHistoryResource(UUID fileId, UUID userId, Action action, String message) {
}
