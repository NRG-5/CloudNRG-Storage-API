package com.cloudnrg.api.history.domain.model.commands;

import com.cloudnrg.api.history.domain.model.valueobjects.Action;

import java.util.UUID;

public record CreateObjectHistoryCommand(UUID fileId, UUID userId, Action action) {
}
