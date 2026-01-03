/*
 * Copyright 2025 Razacki KOUNASSO
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.olakouns.filter;

import io.github.olakouns.representation.PagedResponse;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * A fluent builder for executing paginated, filtered, and sorted list queries.
 * <p>
 * This builder simplifies the construction of complex API requests by handling
 * query parameters for pagination, sorting, and advanced filtering.
 * </p>
 *
 * @param <T> The type of the resource in the paginated response.
 * @author Razacki KOUNASSO
 * @since 1.0.0
 */
public class ListQueryBuilder <T> {
    private final WebTarget target;
    private final GenericType<PagedResponse<T>> responseType;


    private String page;
    private Integer size;
    private final Map<String, String> filters = new HashMap<>();
    private final List<String> sortFields = new ArrayList<>();

    public ListQueryBuilder(WebTarget target, GenericType<PagedResponse<T>> responseType) {
        this.target = target;
        this.responseType = responseType;
    }

    /**
     * Sets the page number or identifier for pagination.
     *
     * @param page the page value (e.g., "1" or a cursor/token).
     * @return this builder instance.
     */
    public ListQueryBuilder<T> page(String page) {
        this.page = page;
        return this;
    }

    /**
     * Sets the number of items to return per page.
     *
     * @param size the maximum number of items in the response.
     * @return this builder instance.
     */
    public ListQueryBuilder<T> size(int size) {
        this.size = size;
        return this;
    }


    /**
     * Configures filters using a {@link FilterBuilder} via a lambda expression.
     * <p>Example:</p>
     * <pre>
     * queryBuilder.filter(f -> f.eq("status", "ACTIVE").gt("amount", 100));
     * </pre>
     *
     * @param filterConfig a consumer that accepts a {@link FilterBuilder}.
     * @return this builder instance.
     */
    public ListQueryBuilder<T> filter(Consumer<FilterBuilder> filterConfig) {
        FilterBuilder builder = new FilterBuilder();
        filterConfig.accept(builder);
        this.filters.putAll(builder.build());
        return this;
    }


    /**
     * Adds a collection of pre-defined filters to the query.
     * <p>
     * This method is useful if you have already built a map of filters using
     * {@link FilterBuilder#build()} or if you are receiving filters from an external source.
     * </p>
     *
     * @param filters a map containing filter keys (e.g., "amount[gte]") and their values.
     * If null, the method does nothing.
     * @return this builder instance.
     */
    public ListQueryBuilder<T> filters(Map<String, String> filters) {
        if (filters != null) {
            this.filters.putAll(filters);
        }
        return this;
    }

    /**
     * Adds a field to sort in ascending order.
     *
     * @param field the field name (e.g., "createdAt").
     * @return this builder instance.
     */
    public ListQueryBuilder<T> sortAsc(String field) {
        if (field != null && !field.trim().isEmpty()) {
            sortFields.add(field);
        }
        return this;
    }

    /**
     * Adds a field to sort in descending order.
     * The field will be prefixed with '-' (e.g., "-createdAt").
     *
     * @param field the field name.
     * @return this builder instance.
     */
    public ListQueryBuilder<T> sortDesc(String field) {
        if (field != null && !field.trim().isEmpty()) {
            sortFields.add("-" + field);
        }
        return this;
    }


    /**
     * Adds a sorting criteria for a specific field with a dynamic direction.
     * <p>
     * This is a convenience method that delegates to {@link #sortAsc(String)}
     * or {@link #sortDesc(String)} based on the boolean flag.
     * </p>
     *
     * @param field     the name of the field to sort by.
     * @param ascending {@code true} for ascending order, {@code false} for descending order.
     * @return this builder instance.
     */
    public ListQueryBuilder<T> sort(String field, boolean ascending) {
        if (ascending) {
            return sortAsc(field);
        } else {
            return sortDesc(field);
        }
    }

    /**
     * Réinitialise uniquement les tris
     */
    public ListQueryBuilder<T> clearSort() {
        sortFields.clear();
        return this;
    }

    /**
     * Adds multiple fields with their respective sort directions.
     * <p>Example:</p>
     * <pre>
     * queryBuilder.sort("type", true, "dateCreation", false);
     * // Result: sort=type,-dateCreation
     * </pre>
     *
     * @param fieldsAndDirections pairs of String (field) and Boolean (true for ASC, false for DESC).
     * @return this builder instance.
     * @throws IllegalArgumentException if the number of arguments is not even.
     */
    public ListQueryBuilder<T> sort(Object... fieldsAndDirections) {
        if (fieldsAndDirections != null && fieldsAndDirections.length % 2 == 0) {
            for (int i = 0; i < fieldsAndDirections.length; i += 2) {
                String field = String.valueOf(fieldsAndDirections[i]);
                boolean ascending = (Boolean) fieldsAndDirections[i + 1];
                sort(field, ascending);
            }
        }
        return this;
    }


    /**
     * Executes the built query against the PI-SPI API and returns a paginated response.
     *
     * @return a {@link PagedResponse} containing the list of items and metadata.
     * @throws RuntimeException if the API request fails.
     */
    public PagedResponse<T> execute() {
        WebTarget queryTarget = target;

        if (page != null) {
            queryTarget = queryTarget.queryParam("page", page);
        }
        if (size != null) {
            queryTarget = queryTarget.queryParam("size", size);
        }

        if (!sortFields.isEmpty()) {
            queryTarget = queryTarget.queryParam("sort", String.join(",", sortFields));
        }

        for (Map.Entry<String, String> entry : filters.entrySet()) {
            queryTarget = queryTarget.queryParam(entry.getKey(), entry.getValue());
        }

        return queryTarget.request(MediaType.APPLICATION_JSON).get(responseType);
    }

}
