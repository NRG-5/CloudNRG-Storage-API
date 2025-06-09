package com.cloudnrg.api.auditlog.domain.model.aggregates;

import com.cloudnrg.api.auditlog.domain.model.valueobjects.AuditAction;
import com.cloudnrg.api.auditlog.domain.model.valueobjects.AuditTargetType;
import com.cloudnrg.api.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class AuditLog extends AuditableAbstractAggregateRoot<AuditLog> {

    @Column(nullable = false)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuditAction action;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AuditTargetType target;

    @Column(nullable = false)
    private String targetId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    public AuditLog(UUID userId, AuditAction action, AuditTargetType target, String targetId, String description) {
        this.userId = userId;
        this.action = action;
        this.target = target;
        this.targetId = targetId;
        this.description = description;
    }
}
