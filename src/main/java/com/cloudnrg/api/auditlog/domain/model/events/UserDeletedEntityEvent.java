package com.cloudnrg.api.auditlog.domain.model.events;

import com.cloudnrg.api.auditlog.domain.model.valueobjects.AuditTargetType;
import lombok.Getter;

import org.springframework.context.ApplicationEvent;

import java.util.UUID;

@Getter

public class UserDeletedEntityEvent extends ApplicationEvent {

    private final UUID userId;
    private final AuditTargetType target;
    private final UUID targetId;
    private final String description;

    public UserDeletedEntityEvent(Object source, UUID userId, AuditTargetType target, UUID targetId, String description) {
        super(source);
        this.userId = userId;
        this.target = target;
        this.targetId = targetId;
        this.description = description;
    }


}
