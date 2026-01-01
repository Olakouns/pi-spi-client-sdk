package io.github.razacki.filter;

import java.util.HashMap;
import java.util.Map;

public class FilterBuilder {
    private final Map<String, String> filters = new HashMap<>();

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

    public Map<String, String> build() {
        return new HashMap<>(filters);
    }

    public Map<String, String> buildFiltersOnly() {
        return new HashMap<>(filters);
    }

    public FilterBuilder clear() {
        filters.clear();
        return this;
    }
}
