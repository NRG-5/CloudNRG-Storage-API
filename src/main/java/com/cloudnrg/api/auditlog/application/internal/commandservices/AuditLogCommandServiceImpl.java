package com.cloudnrg.api.auditlog.application.internal.commandservices;

import com.cloudnrg.api.auditlog.domain.model.aggregates.AuditLog;
import com.cloudnrg.api.auditlog.domain.model.commands.RegisterAuditLogCommand;

import com.cloudnrg.api.auditlog.domain.services.AuditLogCommandService;
import com.cloudnrg.api.auditlog.infrastructure.persistence.jpa.repositories.AuditLogRepository;
import org.springframework.stereotype.Service;

@Service
public class AuditLogCommandServiceImpl implements AuditLogCommandService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogCommandServiceImpl(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Override
    public void handle(RegisterAuditLogCommand command) {
        var log = new AuditLog(
                command.userId(),
                command.action(),
                command.target(),
                command.targetId(),
                command.description()
        );
        auditLogRepository.save(log);
    }
}
