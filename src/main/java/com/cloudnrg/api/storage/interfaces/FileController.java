package com.cloudnrg.api.storage.interfaces;

import com.cloudnrg.api.storage.domain.model.commands.CreateFileCommand;
import com.cloudnrg.api.storage.domain.model.commands.DeleteFileByIdCommand;
import com.cloudnrg.api.storage.domain.model.commands.UpdateFileFolderCommand;
import com.cloudnrg.api.storage.domain.model.commands.UpdateFileNameCommand;
import com.cloudnrg.api.storage.domain.model.queries.GetFilesByFolderIdQuery;
import com.cloudnrg.api.storage.domain.services.FileCommandService;
import com.cloudnrg.api.storage.domain.services.FileQueryService;
import com.cloudnrg.api.storage.interfaces.resources.FileResource;
import com.cloudnrg.api.storage.interfaces.transform.FileResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "*", methods = { RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE })
@RestController
@RequestMapping(value = "/api/v1/files", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "File Controller", description = "Files Management Endpoints")
public class FileController {

    private final FileCommandService fileCommandService;
    private final FileQueryService fileQueryService;

    public FileController(FileCommandService fileCommandService, FileQueryService fileQueryService) {
        this.fileCommandService = fileCommandService;
        this.fileQueryService = fileQueryService;
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
            @RequestParam("userId") UUID userId,
            @RequestParam("folderId") UUID folderId

    ) {

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

    @Operation(summary = "Update file folder", description = "Updates the folder of a file by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File folder updated successfully"),
            @ApiResponse(responseCode = "404", description = "File not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
    })
    @PutMapping(value = "/{fileId}/folder", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FileResource> updateFileFolder(@PathVariable UUID fileId, @RequestParam("folderId") UUID folderId) {
        try {
            var updateFileFolderCommand = new UpdateFileFolderCommand(fileId, folderId);
            var updatedFile = fileCommandService.handle(updateFileFolderCommand);
            if (updatedFile.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            var fileResource = FileResourceFromEntityAssembler.toResourceFromEntity(updatedFile.get(), "ok");
            return ResponseEntity.ok(fileResource);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new FileResource(null, "Error updating file folder"));
        }
    }

    @Operation(summary = "Delete file by ID", description = "Delete a file by its ID")
    @DeleteMapping(value = "/{fileId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse( responseCode = "204", description = "File deleted successfully"),
            @ApiResponse( responseCode = "404", description = "File not found"),
            @ApiResponse( responseCode = "401", description = "Unauthorized"),
    })
    public ResponseEntity<?> deleteFileById(@PathVariable UUID fileId) {
        try {
            fileCommandService.handle(new DeleteFileByIdCommand(fileId));
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

}
