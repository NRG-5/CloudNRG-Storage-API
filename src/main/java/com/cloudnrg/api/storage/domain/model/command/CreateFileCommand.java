package com.cloudnrg.api.storage.domain.model.command;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public record CreateFileCommand(
        MultipartFile file,
        UUID userId
) {
}
