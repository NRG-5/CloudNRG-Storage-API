package com.cloudnrg.api.storage.domain.model.commands;

import java.util.UUID;

public record UpdateFileNameCommand(UUID fileId, String name) {
}
