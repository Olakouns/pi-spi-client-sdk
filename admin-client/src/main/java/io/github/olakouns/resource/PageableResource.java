package io.github.olakouns.resource;

import io.github.olakouns.representation.PagedResponse;

public interface PageableResource<T> {
    PagedResponse<T> list(String page, int size);
}