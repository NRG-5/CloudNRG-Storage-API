package com.cloudnrg.api.auditlog.domain.model.events;

import java.util.UUID;

public class UserLoginEvent {

    private final UUID userId;

    public UserLoginEvent(UUID userId) {
        this.userId = userId;
    }

    public UUID getUserId() {
        return userId;
    }
}

