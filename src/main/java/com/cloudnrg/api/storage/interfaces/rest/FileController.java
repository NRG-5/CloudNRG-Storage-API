package com.cloudnrg.api.storage.interfaces.rest;

import com.cloudnrg.api.shared.infrastructure.ratelimiting.bucket4j.configuration.RateLimitConfig;
import com.cloudnrg.api.shared.interfaces.rest.MessageResource;
import com.cloudnrg.api.storage.application.internal.outboundservices.acl.ExternalUserService;
import com.cloudnrg.api.storage.domain.model.commands.CreateFileCommand;
import com.cloudnrg.api.storage.domain.model.commands.DeleteFileByIdCommand;
import com.cloudnrg.api.storage.domain.model.commands.UpdateFileFolderCommand;
import com.cloudnrg.api.storage.domain.model.commands.UpdateFileNameCommand;
import com.cloudnrg.api.storage.domain.model.queries.GetFileByIdQuery;
import com.cloudnrg.api.storage.domain.model.queries.GetFilesByFolderIdQuery;
import com.cloudnrg.api.storage.domain.services.FileCommandService;
import com.cloudnrg.api.storage.domain.services.FileQueryService;
import com.cloudnrg.api.storage.interfaces.rest.resources.BatchDeleteFilesResource;
import com.cloudnrg.api.storage.interfaces.rest.resources.BatchUpdateFileParentFolderResource;
import com.cloudnrg.api.storage.interfaces.rest.resources.FileResource;
import com.cloudnrg.api.storage.interfaces.rest.transform.FileResourceFromEntityAssembler;
import io.github.bucket4j.Bucket;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@CrossOrigin(origins = "*", methods = { RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE })
@RestController
@RequestMapping(value = "/api/v1/files", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "File Controller", description = "Files Management Endpoints")
public class FileController {

    private final FileCommandService fileCommandService;
    private final FileQueryService fileQueryService;
    private final ExternalUserService externalUserService;


    public FileController(
            FileCommandService fileCommandService,
            FileQueryService fileQueryService,
            ExternalUserService externalUserService
    ) {
        this.fileCommandService = fileCommandService;
        this.fileQueryService = fileQueryService;
        this.externalUserService = externalUserService;
    }


    @Operation(summary = "Upload a file", description = "Upload a file with metadata")
    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE, // Explicitly set content type
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse( responseCode = "201", description = "File uploaded successfully"),
            @ApiResponse( responseCode = "400", description = "Invalid input data"),
            @ApiResponse( responseCode = "401", description = "Unauthorized"),
    })
    public ResponseEntity<FileResource> uploadFile(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam("folderId") UUID folderId

    ) {
        var username = userDetails.getUsername();
        var userId = externalUserService.fetchUserByUsername(username);

        if (userId == null) {
            return ResponseEntity.notFound().build();
        }

        var createFileCommand = new CreateFileCommand(
                file,
                userId,
                folderId
        );

        var cloudFile = fileCommandService.handle(createFileCommand);

        if (cloudFile.isEmpty()) {
            return ResponseEntity.badRequest().body(new FileResource(null, "File upload failed"));
        }

        var fileResource = FileResourceFromEntityAssembler.toResourceFromEntity(cloudFile.get(), "ok");

        return new ResponseEntity<>(fileResource, HttpStatus.CREATED);
    }

    @Operation(summary = "Get files by folder ID", description = "Retrieve files associated with a specific folder ID")
    @GetMapping(value = "/folder/{folderId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse( responseCode = "200", description = "Files retrieved successfully"),
            @ApiResponse( responseCode = "404", description = "Folder not found"),
            @ApiResponse( responseCode = "401", description = "Unauthorized"),
    })
    public ResponseEntity<List<FileResource>> GetFilesByFolderId(
            @PathVariable UUID folderId
    ) {

        var getFilesByFolderIdQuery = new GetFilesByFolderIdQuery(folderId);

        var files = fileQueryService.handle(getFilesByFolderIdQuery);

        var fileResources = files.stream()
                .map(file -> FileResourceFromEntityAssembler.toResourceFromEntity(file, "ok"))
                .toList();

        return ResponseEntity.ok(fileResources);

    }

    @Operation(summary = "Update file name", description = "Updates the name of a file by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File name updated successfully"),
            @ApiResponse(responseCode = "404", description = "File not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
    })
    @PutMapping(value = "/{fileId}/name", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FileResource> updateFileName(@PathVariable UUID fileId, @RequestParam("fileName") String fileName) {
        try {
            var updateFileNameCommand = new UpdateFileNameCommand(fileId, fileName);
            var updatedFile = fileCommandService.handle(updateFileNameCommand);
            if (updatedFile.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            var fileResource = FileResourceFromEntityAssembler.toResourceFromEntity(updatedFile.get(), "ok");
            return ResponseEntity.ok(fileResource);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new FileResource(null, "Error updating file name"));
        }
    }


    @Operation(summary = "Get file by ID", description = "Retrieve a file by its ID")
    @GetMapping(value = "/{fileId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "File not found")
    })
    public ResponseEntity<byte[]> getFileById(@PathVariable UUID fileId ) {



        var getFileByIdQuery = new GetFileByIdQuery(fileId);

        var fileOptional = fileQueryService.handle(getFileByIdQuery);

        if (fileOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }


        var file = fileOptional.get();
        Path filePath = Paths.get(file.getPath());

        // Check if file exists on disk
        if (!Files.exists(filePath)) {
            return ResponseEntity.notFound().build();
        }

        // Read file content
        byte[] fileContent = null;
        try {
            fileContent = Files.readAllBytes(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        // Set appropriate headers
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getMimeType()))
                .contentLength(file.getSize())
                .header("Content-Disposition", "inline; filename=\"" + file.getFilename() + "\"")
                .body(fileContent);

    }

    @Operation(summary = "Update files folder", description = "Update the parent folder of multiple files")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Files updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PutMapping("/batch/parent")
    public ResponseEntity<List<FileResource>> batchUpdateFileFolders(@RequestBody BatchUpdateFileParentFolderResource resource) {
        var updatedFiles = resource.fileIds().stream()
                .map(id -> fileCommandService.handle(new UpdateFileFolderCommand(id, resource.newParentFolderId())))
                .filter(Optional::isPresent)
                .map(opt -> FileResourceFromEntityAssembler.toResourceFromEntity(opt.get(), "ok"))
                .toList();
        return ResponseEntity.ok(updatedFiles);
    }

    @Operation(summary = "Batch delete files", description = "Delete multiple files by their IDs")
    @DeleteMapping("/batch")
    public ResponseEntity<MessageResource> batchDeleteFiles(@RequestBody BatchDeleteFilesResource resource) {
        resource.fileIds().forEach(fileId -> {
            try {
                fileCommandService.handle(new DeleteFileByIdCommand(fileId));
            } catch (Exception e) {
                throw new RuntimeException("Error deleting file with ID " + fileId + ": " + e.getMessage());
            }
        });
        return ResponseEntity.ok(new MessageResource("Files deleted successfully"));
    }
}
