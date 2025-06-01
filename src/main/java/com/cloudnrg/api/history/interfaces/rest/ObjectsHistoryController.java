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
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/history", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Objects History Controller", description = "Objects History Management Endpoint")
public class ObjectsHistoryController {
    private final ObjectHistoryCommandService commandService;
    private final ObjectHistoryQueryService queryService;

    @PostMapping
    public ResponseEntity<ObjectHistoryResource> createObjectHistory(@RequestBody CreateObjectHistoryResource resource) {
        var createObjectHistoryCommand = CreateObjectHistoryCommandFromResourceAssembler.toCommandFromResource(resource);
        var objectHistory = commandService.handle(createObjectHistoryCommand);
        if (objectHistory.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        var objectHistoryResource = ObjectHistoryResourceFromEntityAssembler.toResourceFromEntity(objectHistory.get());
        return new ResponseEntity<>(objectHistoryResource, HttpStatus.CREATED);
    }

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
