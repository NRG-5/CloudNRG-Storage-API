package com.cloudnrg.api.storage.application.acl;

import com.cloudnrg.api.storage.domain.model.aggregates.CloudFile;
import com.cloudnrg.api.storage.domain.model.commands.CreateFileCommand;
import com.cloudnrg.api.storage.domain.model.commands.DeleteFileByIdCommand;
import com.cloudnrg.api.storage.domain.model.commands.UpdateFileFolderCommand;
import com.cloudnrg.api.storage.domain.model.commands.UpdateFileNameCommand;
import com.cloudnrg.api.storage.domain.model.queries.GetFileByIdQuery;
import com.cloudnrg.api.storage.domain.model.queries.GetFilesByFolderIdQuery;
import com.cloudnrg.api.storage.domain.services.FileCommandService;
import com.cloudnrg.api.storage.domain.services.FileQueryService;
import com.cloudnrg.api.storage.interfaces.acl.FileContextFacade;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class FileContextFacadeImpl implements FileContextFacade {
    private final FileCommandService fileCommandService;
    private final FileQueryService fileQueryService;

    @Override
    public UUID createFile(MultipartFile file, UUID userId, UUID folderId) {
        var fileResult = fileCommandService.handle(new CreateFileCommand(file, userId, folderId));
        if(fileResult.isEmpty()) {
            throw new RuntimeException("Failed to create file");
        }
        return fileResult.get().getId();
    }

    @Override
    public void deleteFileById(UUID fileId) {
        fileCommandService.handle(new DeleteFileByIdCommand(fileId));
    }

    @Override
    public Optional<CloudFile> getFileById(UUID fileId) {
        return fileQueryService.handle(new GetFileByIdQuery(fileId));
    }

    @Override
    public List<CloudFile> getFilesByFolderId(UUID folderId) {
        return fileQueryService.handle(new GetFilesByFolderIdQuery(folderId));
    }

    @Override
    public void updateFileName(UUID fileId, String newName) {
        fileCommandService.handle(new UpdateFileNameCommand(fileId, newName));
    }

    @Override
    public void updateFileParentFolder(UUID fileId, UUID newFolderId) {
        fileCommandService.handle(new UpdateFileFolderCommand(fileId, newFolderId));
    }
}
