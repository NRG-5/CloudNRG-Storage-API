package com.cloudnrg.api.auditlog.domain.services;


import com.cloudnrg.api.auditlog.domain.model.commands.RegisterAuditLogCommand;




public interface AuditLogCommandService {
    void handle(RegisterAuditLogCommand command);
}
