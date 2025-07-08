package com.cloudnrg.api.analytics.application.internal.queryservices;

import com.cloudnrg.api.analytics.application.internal.outboundservices.acl.ExternalFileService;
import com.cloudnrg.api.analytics.domain.model.queries.GetFilesByMimeTypeByUserIdQuery;
import com.cloudnrg.api.analytics.domain.model.queries.GetTotalCountFilesByUserIdQuery;
import com.cloudnrg.api.analytics.domain.model.queries.GetTotalCountFoldersByUserIdQuery;
import com.cloudnrg.api.analytics.domain.model.queries.GetTotalSizeFilesByUserIdQuery;
import com.cloudnrg.api.analytics.domain.model.services.AnalyticsQueryService;
import com.cloudnrg.api.iam.application.internal.outboundservices.acl.ExternalFolderService;
import com.cloudnrg.api.shared.application.external.outboundedservices.ExternalIamService;
import com.cloudnrg.api.storage.application.internal.outboundservices.acl.ExternalUserService;
import com.cloudnrg.api.storage.domain.model.aggregates.CloudFile;
import com.cloudnrg.api.storage.infrastructure.persistence.jpa.repositories.CloudFileRepository;
import com.cloudnrg.api.storage.infrastructure.persistence.jpa.repositories.FolderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class AnalyticsQueryServiceImpl implements AnalyticsQueryService {
    private final ExternalFileService externalFileService;
    private final ExternalFolderService externalFolderService;
    private final ExternalUserService externalUserService;

    @Override
    public Integer handle(GetTotalCountFilesByUserIdQuery query) {
        var userResult = externalUserService.fetchUserById(query.userId());

        if (userResult == null) {
            throw new IllegalArgumentException("User with ID " + query.userId() + " does not exist.");
        }

        var files = externalFileService.fetchAllFilesByUserId(userResult.getId());

        return files.size();
    }

    @Override
    public Long handle(GetTotalSizeFilesByUserIdQuery query) {
        var userResult = externalUserService.fetchUserById(query.userId());

        if (userResult == null) {
            throw new IllegalArgumentException("User with ID " + query.userId() + " does not exist.");
        }

        var files = externalFileService.fetchAllFilesByUserId(userResult.getId());

        return files.stream()
                .mapToLong(file -> file.getSize() != null ? file.getSize() : 0L)
                .sum();
    }

    @Override
    public List<String> handle(GetFilesByMimeTypeByUserIdQuery query) {
        var userResult = externalUserService.fetchUserById(query.userId());

        if (userResult == null) {
            throw new IllegalArgumentException("User with ID " + query.userId() + " does not exist.");
        }

        var files = externalFileService.fetchAllFilesByUserId(userResult.getId());

        return files.stream().map(CloudFile::getMimeType)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
    }

    @Override
    public Integer handle(GetTotalCountFoldersByUserIdQuery query) {
        var userResult = externalUserService.fetchUserById(query.userId());

        if (userResult == null) {
            throw new IllegalArgumentException("User with ID " + query.userId() + " does not exist.");
        }

        var folders = externalFolderService.fetchAllFoldersByUserId(userResult.getId());

        return folders.size();
    }
}
