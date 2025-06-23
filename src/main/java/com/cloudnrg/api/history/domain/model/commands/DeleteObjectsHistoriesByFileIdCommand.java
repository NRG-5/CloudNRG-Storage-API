package com.cloudnrg.api.history.domain.model.commands;

import java.util.UUID;

public record DeleteObjectsHistoriesByFileIdCommand(UUID fileId) {
}
