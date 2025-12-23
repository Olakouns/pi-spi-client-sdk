package io.github.resource;

import io.github.filter.ListQueryBuilder;
import io.github.representation.CompteRepresentation;
import io.github.representation.PagedResponse;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;

public class ComptesResourceWrapper {
    private final ComptesResource proxy;
    private final WebTarget target;

    public ComptesResourceWrapper(ComptesResource proxy, WebTarget target) {
        this.proxy = proxy;
        this.target = target;
    }

    public PagedResponse<CompteRepresentation> list(int page, int size) {
        return proxy.list(page, size);
    }

    public ListQueryBuilder<CompteRepresentation> list() {
        return new ListQueryBuilder<>(
                target,
                new GenericType<PagedResponse<CompteRepresentation>>() {}
        );
    }
}
