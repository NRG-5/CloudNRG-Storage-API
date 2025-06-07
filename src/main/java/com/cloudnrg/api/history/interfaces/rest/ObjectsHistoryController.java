package com.cloudnrg.api.history.interfaces.rest;

import com.cloudnrg.api.history.domain.model.queries.GetAllObjectsHistoryByFileIdQuery;
import com.cloudnrg.api.history.domain.model.queries.GetAllObjectsHistoryByUserIdQuery;
import com.cloudnrg.api.history.domain.model.queries.GetObjectHistoryByIdQuery;
import com.cloudnrg.api.history.domain.services.ObjectHistoryCommandService;
import com.cloudnrg.api.history.domain.services.ObjectHistoryQueryService;
import com.cloudnrg.api.history.interfaces.rest.resources.CreateObjectHistoryResource;
import com.cloudnrg.api.history.interfaces.rest.resources.ObjectHistoryResource;
import com.cloudnrg.api.history.interfaces.rest.transform.CreateObjectHistoryCommandFromResourceAssembler;
import com.cloudnrg.api.history.interfaces.rest.transform.ObjectHistoryResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "*", methods = { RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE })
@RestController
@RequestMapping(value = "/api/v1/history", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Objects History", description = "Objects History Management Endpoint")
@AllArgsConstructor
public class ObjectsHistoryController {
    private final ObjectHistoryCommandService commandService;
    private final ObjectHistoryQueryService queryService;

    @PostMapping
    @Operation(summary = "Create a new Object History", description = "Create a new Object History record with the provided details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Object History created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ObjectHistoryResource> createObjectHistory(@RequestBody CreateObjectHistoryResource resource) {
        var createObjectHistoryCommand = CreateObjectHistoryCommandFromResourceAssembler.toCommandFromResource(resource);
        var objectHistory = commandService.handle(createObjectHistoryCommand);
        if (objectHistory.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        var objectHistoryResource = ObjectHistoryResourceFromEntityAssembler.toResourceFromEntity(objectHistory.get());
        return new ResponseEntity<>(objectHistoryResource, HttpStatus.CREATED);
    }

    @Operation(summary = "Get Object History by ID", description = "Retrieve an Object History record by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Object History found"),
            @ApiResponse(responseCode = "404", description = "Object History not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ObjectHistoryResource> getObjectHistoryById(@PathVariable UUID id) {
        var getObjectHistoryByIdQuery = new GetObjectHistoryByIdQuery(id);
        var objectHistory = queryService.handle(getObjectHistoryByIdQuery);
        if (objectHistory.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var objectHistoryResource = ObjectHistoryResourceFromEntityAssembler.toResourceFromEntity(objectHistory.get());
        return ResponseEntity.ok(objectHistoryResource);
    }

    @Operation(summary = "Get all Object Histories by User ID", description = "Retrieve all Object History records associated with a specific User ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Object Histories found"),
            @ApiResponse(responseCode = "404", description = "No Object Histories found for the User ID"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{userId}")
    public ResponseEntity<List<ObjectHistoryResource>> getAllObjectsHistoryByUserId(@PathVariable UUID userId) {
        var getAllObjectsHistoryQuery = new GetAllObjectsHistoryByUserIdQuery(userId);
        var objectsHistory = queryService.handle(getAllObjectsHistoryQuery);
        if (objectsHistory.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var objectsHistoryResources = objectsHistory.stream()
                .map(ObjectHistoryResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(objectsHistoryResources);
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<List<ObjectHistoryResource>> getAllObjectsHistoryByFileId(@PathVariable UUID fileId) {
        var getAllObjectsHistoryQuery = new GetAllObjectsHistoryByFileIdQuery(fileId);
        var objectsHistory = queryService.handle(getAllObjectsHistoryQuery);
        if (objectsHistory.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var objectsHistoryResources = objectsHistory.stream()
                .map(ObjectHistoryResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(objectsHistoryResources);
    }
}
