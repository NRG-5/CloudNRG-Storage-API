package com.cloudnrg.api.storage.application.internal.queryservices;

import com.cloudnrg.api.storage.domain.model.aggregates.Folder;
import com.cloudnrg.api.storage.domain.model.queries.GetFolderHierarchyQuery;
import com.cloudnrg.api.storage.domain.model.queries.GetRootFolderByUserIdQuery;
import com.cloudnrg.api.storage.domain.services.FolderQueryService;
import com.cloudnrg.api.storage.infrastructure.persistence.jpa.repositories.FolderRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class FolderQueryServiceImpl implements FolderQueryService {

    private final FolderRepository folderRepository;

    public FolderQueryServiceImpl(FolderRepository folderRepository) {
        this.folderRepository = folderRepository;
    }

    @Override
    public Optional<Folder> handle(GetRootFolderByUserIdQuery query) {
        return folderRepository.findFolderByUser_IdAndName(query.userId(), "root");
    }

    @Override
    public Optional<List<Folder>> handle(GetFolderHierarchyQuery query) {
        var folderOpt = folderRepository.findFolderById(query.folderId());
        if (folderOpt.isEmpty()) return Optional.empty();
        List<Folder> hierarchy = new ArrayList<>();
        Folder current = folderOpt.get();
        while (current != null) {
            hierarchy.add(current);
            current = current.getParentFolder();
        }
        Collections.reverse(hierarchy);
        return Optional.of(hierarchy);
    }
}
