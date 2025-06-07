package com.cloudnrg.api.storage.interfaces.rest;


import com.cloudnrg.api.storage.domain.model.queries.GetRootFolderByUserIdQuery;
import com.cloudnrg.api.storage.domain.services.FolderQueryService;
import com.cloudnrg.api.storage.interfaces.rest.resources.FolderResource;
import com.cloudnrg.api.storage.interfaces.rest.transform.FolderResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@CrossOrigin(origins = "*", methods = { RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE })
@RestController
@RequestMapping(value = "/api/v1/folders", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Folder Controller", description = "Folder Management Endpoints")
public class FolderController {

    private final FolderQueryService folderQueryService;

    public FolderController(FolderQueryService folderQueryService) {
        this.folderQueryService = folderQueryService;
    }



    //TODO: refactorizar en obtener los archivos por un folderid y si este no es especificado obtener el del root folder
    //get root folder by user id
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
            @RequestParam UUID userId
    ) {

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


    //TODO: implementar endpoint CreateFolder
    //TODO: implementar el endpoint de actualizar el nombre del folder
    //TODO: implmentar endpoint de actualizar el parent folder de un folder
    //TODO: implementar el endpoint de eliminar un folder

}
