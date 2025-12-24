package io.github.resource.wrapper;

import io.github.filter.ListQueryBuilder;
import io.github.representation.PagedResponse;
import io.github.resource.PageableResource;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;

public abstract class BaseWrapper<T, R extends PageableResource<T>> {
    protected final R proxy;
    protected final WebTarget target;

    protected BaseWrapper(R proxy, WebTarget target) {
        this.proxy = proxy;
        this.target = target;
    }

    public PagedResponse<T> list(int page, int size) {
        return proxy.list(page, size); // plus besoin de reflection
    }

    public ListQueryBuilder<T> query() {
        return new ListQueryBuilder<>(target, new GenericType<PagedResponse<T>>() {
        });
    }

    public R proxy() {
        return proxy; // expose le proxy si besoin
    }
}