package com.cloudnrg.api.storage.domain.model.commands;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public record CreateFileCommand(
        MultipartFile file,
        UUID userId,
        UUID folderId
) {
}
