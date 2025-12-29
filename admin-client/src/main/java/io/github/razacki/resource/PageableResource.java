package io.github.razacki.resource;

import io.github.razacki.representation.PagedResponse;

public interface PageableResource<T> {
    PagedResponse<T> list(String page, int size);
}