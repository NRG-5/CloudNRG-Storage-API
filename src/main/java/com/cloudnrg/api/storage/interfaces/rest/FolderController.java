package com.cloudnrg.api.storage.interfaces.rest;


import com.cloudnrg.api.shared.interfaces.rest.MessageResource;
import com.cloudnrg.api.storage.application.internal.outboundservices.acl.ExternalUserService;
import com.cloudnrg.api.storage.domain.model.commands.CreateFolderCommand;
import com.cloudnrg.api.storage.domain.model.commands.DeleteFolderByIdCommand;
import com.cloudnrg.api.storage.domain.model.commands.UpdateFolderNameCommand;
import com.cloudnrg.api.storage.domain.model.commands.UpdateFolderParentCommand;
import com.cloudnrg.api.storage.domain.model.queries.GetFolderAscendantHierarchyQuery;
import com.cloudnrg.api.storage.domain.model.queries.GetFolderByIdQuery;
import com.cloudnrg.api.storage.domain.model.queries.GetFolderDescendantHierarchyQuery;
import com.cloudnrg.api.storage.domain.model.queries.GetRootFolderByUserIdQuery;
import com.cloudnrg.api.storage.domain.services.FolderCommandService;
import com.cloudnrg.api.storage.domain.services.FolderQueryService;
import com.cloudnrg.api.storage.infrastructure.persistence.jpa.repositories.FolderRepository;
import com.cloudnrg.api.storage.interfaces.rest.resources.*;
import com.cloudnrg.api.storage.interfaces.rest.transform.FolderAscendantHierarchyResourceFromEntityAssembler;
import com.cloudnrg.api.storage.interfaces.rest.transform.FolderDescendantHierarchyResourceFromEntityAssembler;
import com.cloudnrg.api.storage.interfaces.rest.transform.FolderResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
import java.util.Optional;
import java.util.UUID;

@CrossOrigin(origins = "*", methods = { RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE })
@RestController
@RequestMapping(value = "/api/v1/folders", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Folder Controller", description = "Folder Management Endpoints")
public class FolderController {

    private final FolderQueryService folderQueryService;
    private final FolderCommandService folderCommandService;
    private final FolderRepository repository;
    private final ExternalUserService externalUserService;

    public FolderController(FolderQueryService folderQueryService, FolderCommandService folderCommandService, FolderRepository repository, ExternalUserService externalUserService) {
        this.folderQueryService = folderQueryService;
        this.folderCommandService = folderCommandService;
        this.repository = repository;
        this.externalUserService = externalUserService;
    }

    @Operation(summary = "Search files by name", description = "Search for files by partial or full name")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Files found successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/search")
    public ResponseEntity<List<FolderResource>> searchFilesByName(
            @RequestParam("query") String query,
            @AuthenticationPrincipal UserDetails userDetails) {

        var userId = externalUserService.fetchUserByUsername(userDetails.getUsername());
        var folders = folderQueryService.searchByName(query, userId);
        var resources = folders.stream()
                .map(FolderResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    @Operation(summary = "Get root folder by user id", description = "Get root folder by user id")
    @GetMapping(
            value = "/root",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse( responseCode = "200", description = "Root folder retrieved successfully"),
            @ApiResponse( responseCode = "400", description = "Invalid input data"),
            @ApiResponse( responseCode = "401", description = "Unauthorized"),
    })
    public ResponseEntity<FolderResource> getRootFolderByUserId(
            @AuthenticationPrincipal UserDetails userDetails
            ) {
        var username = userDetails.getUsername();
        var userId = externalUserService.fetchUserByUsername(username);

        if (userId == null) {
            return ResponseEntity.notFound().build();
        }

        var getRootFolderByUserIdQuery = new GetRootFolderByUserIdQuery(
                userId
        );

        var folder = folderQueryService.handle(getRootFolderByUserIdQuery);

        if (folder.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        var folderResource = FolderResourceFromEntityAssembler.toResourceFromEntity(folder.get());

        return ResponseEntity.ok(folderResource);
    }

    @Operation(summary = "Create folder", description = "Creates a new folder")
    @PostMapping(
            value = "/create",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Folder created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
    })
    public ResponseEntity<FolderResource> createFolder(
            @RequestParam("folderName") String folderName,
            @RequestParam("parentFolderId") UUID parentFolderId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        var username = userDetails.getUsername();
        var userId = externalUserService.fetchUserByUsername(username);

        if (userId == null) {
            return ResponseEntity.notFound().build();
        }

        var command = new CreateFolderCommand(
                userId,
                folderName,
                parentFolderId
        );

        var folder = folderCommandService.handle(command);

        if (folder.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        var resource = FolderResourceFromEntityAssembler.toResourceFromEntity(folder.get());
        return new ResponseEntity<>(resource, HttpStatus.CREATED);
    }

    @Operation(summary = "Update folder name", description = "Updates the name of a folder by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Folder name updated successfully"),
            @ApiResponse(responseCode = "404", description = "Folder not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
    })
    @PutMapping("/{folderId}/name")
    public ResponseEntity<FolderResource> updateFolderName(
            @PathVariable UUID folderId,
            @RequestParam String name) {
        try {
            var command = new UpdateFolderNameCommand(folderId, name);
            var folder = folderCommandService.handle(command);
            if (folder.isEmpty()) return ResponseEntity.notFound().build();
            var resource = FolderResourceFromEntityAssembler.toResourceFromEntity(folder.get());
            return ResponseEntity.ok(resource);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /*@Operation(summary = "Update parent folder", description = "Updates the parent folder of a folder by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Parent folder updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FolderResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Folder or parent not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PutMapping("/{folderId}/parent")
    public ResponseEntity<FolderResource> updateParentFolder(
            @PathVariable UUID folderId,
            @RequestParam UUID parentId) {
        try {
            var command = new UpdateFolderParentCommand(folderId, parentId);
            var folder = folderCommandService.handle(command);
            if (folder.isEmpty()) return ResponseEntity.notFound().build();
            var resource = FolderResourceFromEntityAssembler.toResourceFromEntity(folder.get());
            return ResponseEntity.ok(resource);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }*/

    /*@Operation(summary = "Delete folder", description = "Deletes a folder by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Folder deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Folder not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @DeleteMapping("/{folderId}")
    public ResponseEntity<?> deleteFolder(@PathVariable UUID folderId) {
        try {
            folderCommandService.handle(new DeleteFolderByIdCommand(folderId));
            return ResponseEntity.ok(new MessageResource("Folder deleted successfully"));
        } catch (Exception e) {
            throw new RuntimeException("Error deleting folder: " + e.getMessage());
        }
    }

    @Operation(summary = "Get folder ascendant hierarchy", description = "Retrieves the ascendant hierarchy of a folder by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Folder hierarchy retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FolderAscendantHierarchyResource.class))),
            @ApiResponse(responseCode = "404", description = "Folder not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/{folderId}/ascendant_hierarchy")
    public ResponseEntity<FolderAscendantHierarchyResource> getFolderAscendantHierarchy(@PathVariable UUID folderId) {
        var hierarchyResult = folderQueryService.handle(new GetFolderAscendantHierarchyQuery(folderId));
        if (hierarchyResult.isEmpty()) return ResponseEntity.notFound().build();

        var resource = FolderAscendantHierarchyResourceFromEntityAssembler.toResourceFromHierarchy(hierarchyResult.get());
        return ResponseEntity.ok(resource);
    }*/

    @Operation(summary = "Get folder hierarchy", description = "Retrieves the descendant hierarchy of a root folder by its ID")
    @GetMapping("/hierarchy")
    public ResponseEntity<FolderDescendantHierarchyResource> getFolderDescendantHierarchy(
            @AuthenticationPrincipal UserDetails userDetails
    ) {

        var username = userDetails.getUsername();
        var userId = externalUserService.fetchUserByUsername(username);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var getRootFolderByUserIdQuery = new GetRootFolderByUserIdQuery(userId);
        var rootFolder = folderQueryService.handle(getRootFolderByUserIdQuery);
        if (rootFolder.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var folderId = rootFolder.get().getId();

        var folderResult = folderQueryService.handle(new GetFolderDescendantHierarchyQuery(folderId));
        if (folderResult.isEmpty()) return ResponseEntity.notFound().build();

        var assembler = new FolderDescendantHierarchyResourceFromEntityAssembler(repository);
        var resource = assembler.toResourceFromEntity(folderResult.get());
        return ResponseEntity.ok(resource);
    }

    @Operation(summary = "Update parent folder", description = "Updates the parent folder for multiple folders")
    @PutMapping("/batch/parent")
    public ResponseEntity<List<FolderResource>> batchUpdateParentFolder(@RequestBody BatchUpdateFolderParentResource resource) {
        var updatedFolders = resource.folderIds().stream()
                .map(id -> folderCommandService.handle(new UpdateFolderParentCommand(id, resource.newParentFolderId())))
                .filter(Optional::isPresent)
                .map(opt -> FolderResourceFromEntityAssembler.toResourceFromEntity(opt.get()))
                .toList();
        return ResponseEntity.ok(updatedFolders);
    }

    @Operation(summary = "Batch delete folders", description = "Deletes multiple folders by their IDs")
    @DeleteMapping("/batch")
    public ResponseEntity<MessageResource> batchDeleteFolders(@RequestBody BatchDeleteFoldersResource resource) {
        resource.folderIds().forEach(id -> {
            try {
                folderCommandService.handle(new DeleteFolderByIdCommand(id));
            } catch (Exception e) {
                throw new RuntimeException("Error deleting folder with ID " + id + ": " + e.getMessage());
            }
        });
        return ResponseEntity.ok(new MessageResource("Folders deleted successfully"));
    }


    @Operation(summary = "Get folder by ID", description = "Retrieves a folder by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Folder retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Folder not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping()
    public ResponseEntity<FolderResource> getFolderById(
            @RequestParam(required = false) UUID folderId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {

        String username = userDetails.getUsername();
        var userId = externalUserService.fetchUserByUsername(username);

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (folderId == null) {
            var rootFolderOpt = folderQueryService.handle(new GetRootFolderByUserIdQuery(userId));
            if (rootFolderOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            var folderResource = FolderResourceFromEntityAssembler.toResourceFromEntity(rootFolderOpt.get());
            return ResponseEntity.ok(folderResource);
        }

        var query = new GetFolderByIdQuery(folderId);
        var folderOpt = folderQueryService.handle(query);
        if (folderOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var folderResource = FolderResourceFromEntityAssembler.toResourceFromEntity(folderOpt.get());
        return ResponseEntity.ok(folderResource);

    }

    @Operation(summary = "Get Folders by Parent Id ", description = "Returns a list of folders by their parent folder ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Folders retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Parent folder not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/parent/{parentFolderId}")
    public ResponseEntity<List<FolderResource>> getFoldersByParentFolderId(@PathVariable UUID parentFolderId) {
        var query = new com.cloudnrg.api.storage.domain.model.queries.GetFoldersByParentFolderIdQuery(parentFolderId);
        var folders = folderQueryService.handle(query);

        var folderResources = folders.stream()
                .map(FolderResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(folderResources);
    }



}
