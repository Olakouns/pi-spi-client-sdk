package io.github.razacki.unit.client.filter;

import io.github.filter.FilterBuilder;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("unit")
public class FilterBuilderTest {
    @Test
    void testEqFilter() {
        Map<String, String> filters = new FilterBuilder()
                .eq("numero", "123")
                .build();

        assertEquals(1, filters.size());
        assertEquals("123", filters.get("numero[eq]"));
    }

    @Test
    void testMultipleFilters() {
        Map<String, String> filters = new FilterBuilder()
                .eq("numero", "123")
                .gte("dateOuverture", "2025-01-01")
                .contains("type", "CACC")
                .build();

        assertEquals(3, filters.size());
        assertEquals("123", filters.get("numero[eq]"));
        assertEquals("2025-01-01", filters.get("dateOuverture[gte]"));
        assertEquals("CACC", filters.get("type[contains]"));
    }

    @Test
    void testBetweenAddsTwoFilters() {
        Map<String, String> filters = new FilterBuilder()
                .between("montant", 100, 200)
                .build();

        assertEquals(2, filters.size());
        assertEquals("100", filters.get("montant[gte]"));
        assertEquals("200", filters.get("montant[lte]"));
    }

    @Test
    void testInFilter() {
        Map<String, String> filters = new FilterBuilder()
                .in("type", "CACC", "SVGS")
                .build();

        assertEquals(1, filters.size());
        assertEquals("CACC,SVGS", filters.get("type[in]"));
    }

    @Test
    void testExistsFilter() {
        Map<String, String> filters = new FilterBuilder()
                .exists("statut", true)
                .build();

        assertEquals(1, filters.size());
        assertEquals("true", filters.get("statut[exists]"));
    }
}
