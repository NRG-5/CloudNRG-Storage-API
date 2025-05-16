package com.cloudnrg.api.iam.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

@Getter
public class UserCreationEvent extends ApplicationEvent {

    private UUID userId;

    public UserCreationEvent(Object source, UUID userId) {
        super(source);
        this.userId = userId;
    }
}
