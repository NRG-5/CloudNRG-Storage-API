package com.cloudnrg.api.analytics.domain.model.services;

import com.cloudnrg.api.analytics.domain.model.queries.GetFilesByMimeTypeByUserIdQuery;
import com.cloudnrg.api.analytics.domain.model.queries.GetTotalCountFilesByUserIdQuery;
import com.cloudnrg.api.analytics.domain.model.queries.GetTotalCountFoldersByUserIdQuery;
import com.cloudnrg.api.analytics.domain.model.queries.GetTotalSizeFilesByUserIdQuery;

import java.util.List;

public interface AnalyticsQueryService {
    Integer handle(GetTotalCountFilesByUserIdQuery query);
    Long handle(GetTotalSizeFilesByUserIdQuery query);

    List<String> handle(GetFilesByMimeTypeByUserIdQuery query);
    Integer handle(GetTotalCountFoldersByUserIdQuery query);
}
