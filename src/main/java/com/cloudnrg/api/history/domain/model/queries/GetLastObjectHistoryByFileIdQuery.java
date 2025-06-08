package com.cloudnrg.api.history.domain.model.queries;

import java.util.UUID;

public record GetLastObjectHistoryByFileIdQuery(UUID fileId) {
}
