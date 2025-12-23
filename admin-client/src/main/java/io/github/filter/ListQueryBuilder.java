package io.github.filter;

import io.github.representation.PagedResponse;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ListQueryBuilder <T> {
    private final WebTarget target;
    private final GenericType<PagedResponse<T>> responseType;

    private String page;
    private Integer size;
    private final Map<String, String> filters = new HashMap<>();

    public ListQueryBuilder(WebTarget target, GenericType<PagedResponse<T>> responseType) {
        this.target = target;
        this.responseType = responseType;
    }

    public ListQueryBuilder<T> page(String page) {
        this.page = page;
        return this;
    }

    public ListQueryBuilder<T> size(int size) {
        this.size = size;
        return this;
    }


    public ListQueryBuilder<T> filter(Consumer<FilterBuilder> filterConfig) {
        FilterBuilder builder = new FilterBuilder();
        filterConfig.accept(builder);
        this.filters.putAll(builder.build());
        return this;
    }


    public ListQueryBuilder<T> filters(Map<String, String> filters) {
        if (filters != null) {
            this.filters.putAll(filters);
        }
        return this;
    }

    public PagedResponse<T> execute() {
        WebTarget queryTarget = target;

        if (page != null) {
            queryTarget = queryTarget.queryParam("page", page);
        }
        if (size != null) {
            queryTarget = queryTarget.queryParam("size", size);
        }

        for (Map.Entry<String, String> entry : filters.entrySet()) {
            queryTarget = queryTarget.queryParam(entry.getKey(), entry.getValue());
        }

        return queryTarget.request(MediaType.APPLICATION_JSON).get(responseType);
    }

}
