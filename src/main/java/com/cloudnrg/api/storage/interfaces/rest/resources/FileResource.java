package com.cloudnrg.api.storage.interfaces.rest.resources;

public record FileResource(
        FileDataResource data,
        String status
) {
}
