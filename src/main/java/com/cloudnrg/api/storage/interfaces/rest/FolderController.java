package com.cloudnrg.api.storage.interfaces.rest;

import com.cloudnrg.api.storage.application.internal.outboundservices.acl.ExternalUserService;
import com.cloudnrg.api.storage.domain.model.commands.*;
import com.cloudnrg.api.storage.domain.model.queries.GetFolderHierarchyQuery;
import com.cloudnrg.api.storage.domain.model.queries.GetRootFolderByUserIdQuery;
import com.cloudnrg.api.storage.domain.services.FolderCommandService;
import com.cloudnrg.api.storage.domain.services.FolderQueryService;
import com.cloudnrg.api.storage.interfaces.rest.resources.FolderHierarchyResource;
import com.cloudnrg.api.storage.interfaces.rest.resources.FolderResource;
import com.cloudnrg.api.storage.interfaces.rest.transform.FolderHierarchyResourceFromEntityAssembler;
import com.cloudnrg.api.storage.interfaces.rest.transform.FolderResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@CrossOrigin(origins = "*", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE})
@RestController
@RequestMapping(value = "/api/v1/folders", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Folder Controller", description = "Folder Management Endpoints")
public class FolderController {

    private final FolderQueryService folderQueryService;
    private final FolderCommandService folderCommandService;
    private final ExternalUserService externalUserService;

    public FolderController(FolderQueryService folderQueryService,
                            FolderCommandService folderCommandService,
                            ExternalUserService externalUserService) {
        this.folderQueryService = folderQueryService;
        this.folderCommandService = folderCommandService;
        this.externalUserService = externalUserService;
    }

    @Operation(summary = "Get root folder by user id", description = "Get root folder by user id")
    @GetMapping(value = "/root", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Root folder retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<FolderResource> getRootFolderByUserId(@AuthenticationPrincipal UserDetails userDetails) {
        var userId = externalUserService.fetchUserByUsername(userDetails.getUsername());
        if (userId == null) return ResponseEntity.notFound().build();
        var folder = folderQueryService.handle(new GetRootFolderByUserIdQuery(userId));
        if (folder.isEmpty()) return ResponseEntity.notFound().build();
        var resource = FolderResourceFromEntityAssembler.toResourceFromEntity(folder.get());
        return ResponseEntity.ok(resource);
    }

    @Operation(summary = "Create folder", description = "Creates a new folder")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Folder created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FolderResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Related resource not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping
    public ResponseEntity<FolderResource> createFolder(@RequestBody CreateFolderRequest body,
                                                       @AuthenticationPrincipal UserDetails userDetails) {
        var userId = externalUserService.fetchUserByUsername(userDetails.getUsername());
        var command = new CreateFolderCommand(userId, body.folderName(), body.parentFolderId());
        var folder = folderCommandService.handle(command);
        if (folder.isEmpty()) return ResponseEntity.badRequest().build();
        var resource = FolderResourceFromEntityAssembler.toResourceFromEntity(folder.get());
        return ResponseEntity.status(201).body(resource);
    }

    @Operation(summary = "Update folder name", description = "Updates the name of a folder by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Folder name updated successfully"),
            @ApiResponse(responseCode = "404", description = "Folder not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PutMapping("/{folderId}/name")
    public ResponseEntity<FolderResource> updateFolderName(@PathVariable UUID folderId,
                                                           @RequestParam String name,
                                                           @AuthenticationPrincipal UserDetails userDetails) {
        var userId = externalUserService.fetchUserByUsername(userDetails.getUsername());
        var command = new UpdateFolderNameCommand(folderId, name, userId);
        var folder = folderCommandService.handle(command);
        if (folder.isEmpty()) return ResponseEntity.notFound().build();
        var resource = FolderResourceFromEntityAssembler.toResourceFromEntity(folder.get());
        return ResponseEntity.ok(resource);
    }

    @Operation(summary = "Update parent folder", description = "Updates the parent folder of a folder by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Parent folder updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FolderResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Folder or parent not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PutMapping("/{folderId}/parent")
    public ResponseEntity<FolderResource> updateParentFolder(@PathVariable UUID folderId,
                                                             @RequestParam UUID parentFolderId,
                                                             @AuthenticationPrincipal UserDetails userDetails) {
        var userId = externalUserService.fetchUserByUsername(userDetails.getUsername());
        var command = new UpdateFolderParentCommand(folderId, parentFolderId, userId);
        var folder = folderCommandService.handle(command);
        if (folder.isEmpty()) return ResponseEntity.notFound().build();
        var resource = FolderResourceFromEntityAssembler.toResourceFromEntity(folder.get());
        return ResponseEntity.ok(resource);
    }

    @Operation(summary = "Delete folder", description = "Deletes a folder by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Folder deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Folder not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @DeleteMapping("/{folderId}")
    public ResponseEntity<Void> deleteFolder(@PathVariable UUID folderId,
                                             @AuthenticationPrincipal UserDetails userDetails) {
        var userId = externalUserService.fetchUserByUsername(userDetails.getUsername());
        folderCommandService.handle(new DeleteFolderByIdCommand(folderId, userId));
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get folder hierarchy", description = "Retrieves the hierarchy of a folder by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Folder hierarchy retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FolderHierarchyResource.class))),
            @ApiResponse(responseCode = "404", description = "Folder not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/{folderId}/hierarchy")
    public ResponseEntity<FolderHierarchyResource> getFolderHierarchy(@PathVariable UUID folderId,
                                                                      @AuthenticationPrincipal UserDetails userDetails) {
        var userId = externalUserService.fetchUserByUsername(userDetails.getUsername());
        var hierarchy = folderQueryService.handle(new GetFolderHierarchyQuery(folderId, userId));
        if (hierarchy.isEmpty()) return ResponseEntity.notFound().build();
        var resource = FolderHierarchyResourceFromEntityAssembler.toResourceFromHierarchy(hierarchy.get());
        return ResponseEntity.ok(resource);
    }
}
