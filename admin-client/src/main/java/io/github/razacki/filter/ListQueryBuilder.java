package io.github.razacki.filter;

import io.github.razacki.representation.PagedResponse;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ListQueryBuilder <T> {
    private final WebTarget target;
    private final GenericType<PagedResponse<T>> responseType;


    // TODO : add sort option here and remove from filter builder
    private String page;
    private Integer size;
    private final Map<String, String> filters = new HashMap<>();
    private final List<String> sortFields = new ArrayList<>();

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

    /**
     * Exemple: sortAsc("dateCreation") → sort=dateCreation
     */
    public ListQueryBuilder<T> sortAsc(String field) {
        if (field != null && !field.trim().isEmpty()) {
            sortFields.add(field);
        }
        return this;
    }

    /**
     * Sort in descending order.
     * Exemple: sortDesc("dateCreation") → sort=-dateCreation
     */
    public ListQueryBuilder<T> sortDesc(String field) {
        if (field != null && !field.trim().isEmpty()) {
            sortFields.add("-" + field);
        }
        return this;
    }

    /**
     * Sort in ascending or descending order depending on the boolean value.
     *
     * @param field Le champ à trier
     * @param ascending true pour ascendant, false pour descendant
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
     * Sort by multiple fields with specified directions.
     * Exemple: sort("type", true, "dateCreation", false)
     * Résultat: sort=type,-dateCreation
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
