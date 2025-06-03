package com.cloudnrg.api.auditlog.interfaces.rest;

import com.cloudnrg.api.auditlog.domain.model.queries.GetAuditLogsByUserQuery;
import com.cloudnrg.api.auditlog.domain.services.AuditLogQueryService;

import com.cloudnrg.api.auditlog.interfaces.rest.resources.AuditLogResource;
import com.cloudnrg.api.auditlog.interfaces.rest.transform.AuditLogResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/auditlogs", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Audit Log Controller", description = "Audit Log Management Endpoints")
public class AuditLogController {

    private final AuditLogQueryService auditLogQueryService;

    public AuditLogController(AuditLogQueryService auditLogQueryService) {
        this.auditLogQueryService = auditLogQueryService;
    }

    @Operation(summary = "Get audit logs by user ID", description = "Retrieve audit logs for a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logs retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Logs not found")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AuditLogResource>> getLogsByUser(@PathVariable UUID userId) {
        var query = new GetAuditLogsByUserQuery(userId);
        var logs = auditLogQueryService.handle(query);
        var resources = logs.stream()
                .map(AuditLogResourceAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }
}
