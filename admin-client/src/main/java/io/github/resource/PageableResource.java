package io.github.resource;

import io.github.representation.PagedResponse;

public interface PageableResource<T> {
    PagedResponse<T> list(int page, int size);
}