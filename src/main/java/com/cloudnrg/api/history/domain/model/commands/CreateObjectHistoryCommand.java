package com.cloudnrg.api.history.domain.model.commands;

import java.util.UUID;

public record CreateObjectHistoryCommand(UUID fileId, UUID userId, String action) {
}
