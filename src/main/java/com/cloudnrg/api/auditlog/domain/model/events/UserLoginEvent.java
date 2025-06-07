package com.cloudnrg.api.auditlog.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import java.util.UUID;

@Getter
public class UserLoginEvent extends ApplicationEvent {
    private final UUID userId;

    public UserLoginEvent(Object source, UUID userId) {
        super(source);
        this.userId = userId;
    }
}

