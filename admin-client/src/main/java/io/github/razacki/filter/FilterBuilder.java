package io.github.razacki.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilterBuilder {
    private final Map<String, String> filters = new HashMap<>();
    private final List<String> sortFields = new ArrayList<>();

    public FilterBuilder eq(String field, Object value) {
        return add(field, "eq", value);
    }

    public FilterBuilder ne(String field, Object value) {
        return add(field, "ne", value);
    }

    public FilterBuilder gt(String field, Object value) {
        return add(field, "gt", value);
    }

    public FilterBuilder gte(String field, Object value) {
        return add(field, "gte", value);
    }

    public FilterBuilder lt(String field, Object value) {
        return add(field, "lt", value);
    }

    public FilterBuilder lte(String field, Object value) {
        return add(field, "lte", value);
    }

    public FilterBuilder contains(String field, String value) {
        return add(field, "contains", value);
    }

    public FilterBuilder notContains(String field, String value) {
        return add(field, "notContains", value);
    }

    public FilterBuilder beginsWith(String field, String value) {
        return add(field, "beginsWith", value);
    }


    public FilterBuilder endsWith(String field, String value) {
        return add(field, "endsWith", value);
    }

    public FilterBuilder exists(String field, boolean value) {
        return add(field, "exists", value);
    }


    public FilterBuilder in(String field, Object... values) {
        if (values != null && values.length > 0) {
            String joined = String.join(",",
                    java.util.Arrays.stream(values)
                            .map(String::valueOf)
                            .toArray(String[]::new)
            );
            return add(field, "in", joined);
        }
        return this;
    }

    public FilterBuilder between(String field, Object start, Object end) {
        gte(field, start);
        lte(field, end);
        return this;
    }

    public FilterBuilder custom(String field, String operator, Object value) {
        return add(field, operator, value);
    }

    private FilterBuilder add(String field, String operator, Object value) {
        if (value != null) {
            filters.put(field + "[" + operator + "]", String.valueOf(value));
        }
        return this;
    }

    public FilterBuilder where(String field, Object value) {
        if (value != null && field != null) {
            filters.put(field, String.valueOf(value));
        }
        return this;
    }

    /**
     * Exemple: sortAsc("dateCreation") → sort=dateCreation
     */
    public FilterBuilder sortAsc(String field) {
        if (field != null && !field.trim().isEmpty()) {
            sortFields.add(field);
        }
        return this;
    }

    /**
     * Sort in descending order.
     * Exemple: sortDesc("dateCreation") → sort=-dateCreation
     */
    public FilterBuilder sortDesc(String field) {
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
    public FilterBuilder sort(String field, boolean ascending) {
        if (ascending) {
            return sortAsc(field);
        } else {
            return sortDesc(field);
        }
    }

    /**
     * Sort by multiple fields with specified directions.
     * Exemple: sort("type", true, "dateCreation", false)
     * Résultat: sort=type,-dateCreation
     */
    public FilterBuilder sort(Object... fieldsAndDirections) {
        if (fieldsAndDirections != null && fieldsAndDirections.length % 2 == 0) {
            for (int i = 0; i < fieldsAndDirections.length; i += 2) {
                String field = String.valueOf(fieldsAndDirections[i]);
                boolean ascending = (Boolean) fieldsAndDirections[i + 1];
                sort(field, ascending);
            }
        }
        return this;
    }


    public Map<String, String> build() {
        Map<String, String> result = new HashMap<>(filters);

        if (!sortFields.isEmpty()) {
            result.put("sort", String.join(",", sortFields));
        }

        return result;
    }

    public Map<String, String> buildFiltersOnly() {
        return new HashMap<>(filters);
    }

    public String buildSortOnly() {
        return sortFields.isEmpty() ? null : String.join(",", sortFields);
    }

    public FilterBuilder clear() {
        filters.clear();
        sortFields.clear();
        return this;
    }

    public FilterBuilder clearFilters() {
        filters.clear();
        return this;
    }

    /**
     * Réinitialise uniquement les tris
     */
    public FilterBuilder clearSort() {
        sortFields.clear();
        return this;
    }
}
