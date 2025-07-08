package com.cloudnrg.api.analytics.interfaces.rest.resources;

import java.util.Map;

public record FilesByMimeTypeResource(
        Map<String, Long> mimeTypes
) {
}
