package com.cloudnrg.api.storage.interfaces.resources;

public record FileResource(
        FileDataResource data,
        String status
) {
}
