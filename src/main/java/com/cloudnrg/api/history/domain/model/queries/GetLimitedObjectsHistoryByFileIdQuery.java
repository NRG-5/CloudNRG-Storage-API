package com.cloudnrg.api.history.domain.model.queries;

import java.util.UUID;

public record GetLimitedObjectsHistoryByFileIdQuery(UUID fileId, Integer limit) {
}
