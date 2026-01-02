package io.github.olakouns.resource.wrapper;

import io.github.olakouns.filter.ListQueryBuilder;
import io.github.olakouns.representation.PagedResponse;
import io.github.olakouns.resource.PageableResource;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;

public abstract class ListableWrapper<T, R extends PageableResource<T>> extends BaseWrapper<R> {
    private final GenericType<PagedResponse<T>> responseType;

    protected ListableWrapper(R proxy, WebTarget target, GenericType<PagedResponse<T>> responseType) {
        super(proxy, target);
        this.responseType = responseType;
    }

    public PagedResponse<T> list(String page, int size) {
        return proxy.list(page, size);
    }

    public ListQueryBuilder<T> query() {
        return new ListQueryBuilder<>(target, responseType);
    }
}
