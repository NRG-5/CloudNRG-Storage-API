package com.cloudnrg.api.storage.application.internal.commandservices;

import com.cloudnrg.api.iam.infrastructure.persistance.jpa.repositories.UserRepository;
import com.cloudnrg.api.storage.domain.model.aggregates.CloudFile;
import com.cloudnrg.api.storage.domain.model.command.CreateFileCommand;
import com.cloudnrg.api.storage.domain.services.FileCommandService;
import com.cloudnrg.api.storage.infrastructure.persistence.jpa.repositories.CloudFileRepository;
import com.cloudnrg.api.storage.infrastructure.persistence.jpa.repositories.FolderRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;

@Service
public class FileCommandServiceImpl implements FileCommandService {

    private final CloudFileRepository cloudFileRepository;
    private final FolderRepository folderRepository;
    private final UserRepository userRepository;

    private static final String UPLOAD_DIR = "C:\\Users\\Neo\\Documents\\provisional-storage\\";

    public FileCommandServiceImpl(
            CloudFileRepository cloudFileRepository,
            FolderRepository folderRepository,
            UserRepository userRepository) {
        this.cloudFileRepository = cloudFileRepository;
        this.folderRepository = folderRepository;
        this.userRepository = userRepository;
    }


    @Override
    public Optional<CloudFile> handle(CreateFileCommand command) {

        //find the first folder for the user
        var rootFolder = folderRepository.findByUser_IdAndName(command.userId(), "root");
        var user = userRepository.findUserById(command.userId());

        if (rootFolder.isEmpty()) {
            throw new RuntimeException("Root folder not found");
        }

        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        var tempFile = command.file();

        // Calculate MD5 hash
        String md5Hash;
        try {
            md5Hash = DigestUtils.md5Hex(tempFile.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file: " + e.getMessage());
        }

        var file = new CloudFile(
                tempFile.getOriginalFilename(),
                rootFolder.get(),
                user.get(),
                tempFile.getSize(),
                tempFile.getContentType(),
                md5Hash,
                rootFolder.get().getName() + "\\" +
                        tempFile.getOriginalFilename()
        );

        try {

            cloudFileRepository.save(file);

            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(Objects.requireNonNull(tempFile.getOriginalFilename()));
            tempFile.transferTo(filePath.toFile());

        } catch (Exception e) {
            throw new IllegalArgumentException("Error while saving: " + e.getMessage());
        }

        return Optional.of(file);
    }
}
