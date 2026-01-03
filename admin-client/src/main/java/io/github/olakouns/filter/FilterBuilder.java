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

import java.util.HashMap;
import java.util.Map;


/**
 * Utility builder to create complex query filters for the PI-SPI API.
 * <p>
 * This builder follows a fluent API pattern to construct a map of query parameters
 * using the format {@code field[operator]=value}.
 * </p>
 * * <p>Example usage:</p>
 * <pre>
 * Map&lt;String, String&gt; filters = new FilterBuilder()
 * .eq("status", "COMPLETED")
 * .gte("amount", 1000)
 * .build();
 * </pre>
 *
 * @author Razacki KOUNASSO
 * @since 1.0.0
 */
public class FilterBuilder {
    private final Map<String, String> filters = new HashMap<>();

    /**
     * Adds an "equal" filter: {@code field[eq]=value}.
     */
    public FilterBuilder eq(String field, Object value) {
        return add(field, "eq", value);
    }

    /**
     * Adds a "not equal" filter: {@code field[ne]=value}.
     */
    public FilterBuilder ne(String field, Object value) {
        return add(field, "ne", value);
    }

    /**
     * Adds a "greater than" filter: {@code field[gt]=value}.
     */
    public FilterBuilder gt(String field, Object value) {
        return add(field, "gt", value);
    }

    /**
     * Adds a "greater than or equal" filter: {@code field[gte]=value}.
     */
    public FilterBuilder gte(String field, Object value) {
        return add(field, "gte", value);
    }

    /**
     * Adds a "less than" filter: {@code field[lt]=value}.
     */
    public FilterBuilder lt(String field, Object value) {
        return add(field, "lt", value);
    }

    /**
     * Adds a "less than or equal" filter: {@code field[lte]=value}.
     */
    public FilterBuilder lte(String field, Object value) {
        return add(field, "lte", value);
    }

    /**
     * Adds a "contains" filter for string fields: {@code field[contains]=value}.
     */
    public FilterBuilder contains(String field, String value) {
        return add(field, "contains", value);
    }

    /**
     * Adds a "notContains" filter for string fields: {@code field[notContains]=value}.
     */
    public FilterBuilder notContains(String field, String value) {
        return add(field, "notContains", value);
    }

    /**
     * Adds a "beginsWith" filter for string fields: {@code field[beginsWith]=value}.
     */
    public FilterBuilder beginsWith(String field, String value) {
        return add(field, "beginsWith", value);
    }


    /**
     * Adds a "endsWith" filter for string fields: {@code field[endsWith]=value}.
     */
    public FilterBuilder endsWith(String field, String value) {
        return add(field, "endsWith", value);
    }

    /**
     * Adds a "exists" filter for string fields: {@code field[exists]=value}.
     */
    public FilterBuilder exists(String field, boolean value) {
        return add(field, "exists", value);
    }

    /**
     * Adds an "in" filter to match any of the provided values: {@code field[in]=val1,val2...}.
     */
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

    /**
     * Combines GTE and LTE to create a range filter.
     *
     * @param field the field name
     * @param start the minimum value (inclusive)
     * @param end the maximum value (inclusive)
     */
    public FilterBuilder between(String field, Object start, Object end) {
        gte(field, start);
        lte(field, end);
        return this;
    }

    /**
     * Adds a custom filter with a user-defined operator: {@code field[operator]=value}.
     * <p>
     * This is useful for forward compatibility if the API introduces new operators
     * not yet explicitly supported by this builder.
     * </p>
     *
     * @param field    the name of the field to filter.
     * @param operator the operator string (e.g., "like").
     * @param value    the value to filter by.
     * @return this builder instance.
     */
    public FilterBuilder custom(String field, String operator, Object value) {
        return add(field, operator, value);
    }

    private FilterBuilder add(String field, String operator, Object value) {
        if (value != null) {
            filters.put(field + "[" + operator + "]", String.valueOf(value));
        }
        return this;
    }

    /**
     * Adds a raw filter without an operator: {@code field=value}.
     * Useful for standard query parameters.
     */
    public FilterBuilder where(String field, Object value) {
        if (value != null && field != null) {
            filters.put(field, String.valueOf(value));
        }
        return this;
    }

    /**
     * Builds and returns the map of filters.
     * @return a new map containing all configured filters.
     */
    public Map<String, String> build() {
        return new HashMap<>(filters);
    }

    /**
     * Clears all filters currently held by this builder.
     */
    public FilterBuilder clear() {
        filters.clear();
        return this;
    }
}
