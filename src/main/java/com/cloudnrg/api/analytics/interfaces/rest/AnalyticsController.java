package com.cloudnrg.api.analytics.interfaces.rest;

import com.cloudnrg.api.analytics.domain.model.queries.GetFilesByMimeTypeByUserIdQuery;
import com.cloudnrg.api.analytics.domain.model.queries.GetTotalCountFilesByUserIdQuery;
import com.cloudnrg.api.analytics.domain.model.queries.GetTotalCountFoldersByUserIdQuery;
import com.cloudnrg.api.analytics.domain.model.queries.GetTotalSizeFilesByUserIdQuery;
import com.cloudnrg.api.analytics.domain.model.services.AnalyticsQueryService;
import com.cloudnrg.api.analytics.interfaces.rest.resources.FilesByMimeTypeResource;
import com.cloudnrg.api.analytics.interfaces.rest.resources.TotalCountFilesResource;
import com.cloudnrg.api.analytics.interfaces.rest.resources.TotalCountFoldersResource;
import com.cloudnrg.api.analytics.interfaces.rest.resources.TotalSizeFilesResource;
import com.cloudnrg.api.analytics.interfaces.rest.transform.AnalyticsResourceAssembler;
import com.cloudnrg.api.storage.application.internal.outboundservices.acl.ExternalUserService;
import com.cloudnrg.api.storage.domain.model.aggregates.CloudFile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", methods = { RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE })
@RestController
@RequestMapping(value = "/api/v1/analytics", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Analytics Controller", description = "Folder and File Analytics Management Endpoints")
public class AnalyticsController {
    private final AnalyticsQueryService analyticsQueryService;
    private final ExternalUserService externalUserService;

    public AnalyticsController(AnalyticsQueryService analyticsQueryService, ExternalUserService externalUserService) {
        this.analyticsQueryService = analyticsQueryService;
        this.externalUserService = externalUserService;
    }

    @Operation(summary = "Get total size of files", description = "Returns the total size of files for the authenticated user")
    @GetMapping("/files/total-size")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Analytics retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<TotalSizeFilesResource> getTotalFileSize(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String username = userDetails.getUsername();
        UUID userId = externalUserService.fetchUserByUsername(username);

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        long totalSize = analyticsQueryService.handle(new GetTotalSizeFilesByUserIdQuery(userId));

        return ResponseEntity.ok(AnalyticsResourceAssembler.toTotalFileSizeResource(totalSize));
    }

    @Operation(summary = "Get total count of files", description = "Returns the total count of files for the authenticated user")
    @GetMapping("/files/total-count")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Analytics retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<TotalCountFilesResource> getTotalFiles(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String username = userDetails.getUsername();
        UUID userId = externalUserService.fetchUserByUsername(username);

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        int totalFiles = analyticsQueryService.handle(new GetTotalCountFilesByUserIdQuery(userId));

        return ResponseEntity.ok(AnalyticsResourceAssembler.toTotalFilesCountResource(totalFiles));
    }

    @Operation(summary = "Get files distinct by Mime Type", description = "Returns a list of distinct Mime Types of files for the authenticated user")
    @GetMapping("/files/by-mimetype")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Files by Mime Type retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<FilesByMimeTypeResource> getFilesByMimeType(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String username = userDetails.getUsername();
        UUID userId = externalUserService.fetchUserByUsername(username);

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<String> mimeTypes = analyticsQueryService.handle(new GetFilesByMimeTypeByUserIdQuery(userId));

        Map<String, Long> mimeTypeCounts = mimeTypes.stream()
                .collect(Collectors.groupingBy(
                        mimeType -> mimeType,
                        Collectors.counting()
                ));

        return ResponseEntity.ok(AnalyticsResourceAssembler.toFilesByMimeTypeResource(mimeTypeCounts));
    }

    @Operation(summary = "Get total count of folders", description = "Returns the total count of folders for the authenticated user")
    @GetMapping("/folders/total-count")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Analytics retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<TotalCountFoldersResource> getTotalFolders(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String username = userDetails.getUsername();
        UUID userId = externalUserService.fetchUserByUsername(username);

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        int totalFolders = analyticsQueryService.handle(new GetTotalCountFoldersByUserIdQuery(userId));

        return ResponseEntity.ok(AnalyticsResourceAssembler.toTotalFoldersCountResource(totalFolders));
    }
}