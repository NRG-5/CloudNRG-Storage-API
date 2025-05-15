package com.cloudnrg.api.storage.interfaces;

import com.cloudnrg.api.storage.domain.model.command.CreateFileCommand;
import com.cloudnrg.api.storage.domain.services.FileCommandService;
import com.cloudnrg.api.storage.interfaces.resources.FileResource;
import com.cloudnrg.api.storage.interfaces.transform.FileResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@CrossOrigin(origins = "*", methods = { RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE })
@RestController
@RequestMapping(value = "/api/v1/files", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "File Controller", description = "Files Management Endpoints")
public class FileController {

    private final FileCommandService fileCommandService;

    public FileController(FileCommandService fileCommandService) {
        this.fileCommandService = fileCommandService;
    }

    @Operation(summary = "Upload a file", description = "Upload a file with metadata")
    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE, // Explicitly set content type
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<FileResource> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("userId") UUID userId

    ) {

        var createFileCommand = new CreateFileCommand(
                file,
                userId
        );

        var cloudFile = fileCommandService.handle(createFileCommand);

        if (cloudFile.isEmpty()) {
            return ResponseEntity.badRequest().body(new FileResource(null, "File upload failed"));
        }

        var fileResource = FileResourceFromEntityAssembler.toResourceFromEntity(cloudFile.get(), "ok");

        return ResponseEntity.ok(fileResource);
    }





}
