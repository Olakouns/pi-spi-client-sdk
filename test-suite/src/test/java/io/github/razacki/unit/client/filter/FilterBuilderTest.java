package io.github.razacki.unit.client.filter;

import io.github.razacki.filter.FilterBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("unit")
@DisplayName("FilterBuilder Tests")
public class FilterBuilderTest {

    @Test
    @DisplayName("eq filter should add equality condition")
    void testEqFilter() {
        Map<String, String> filters = new FilterBuilder()
                .eq("numero", "123")
                .build();

        assertEquals(1, filters.size());
        assertEquals("123", filters.get("numero[eq]"));
    }

    @Test
    @DisplayName("ne filter should add not-equal condition")
    void testNeFilter() {
        Map<String, String> filters = new FilterBuilder()
                .ne("status", "OUVERT")
                .build();

        assertEquals(1, filters.size());
        assertEquals("OUVERT", filters.get("status[ne]"));
    }

    @Test
    @DisplayName("gt filter should add greater-than condition")
    void testGtFilter() {
        Map<String, String> filters = new FilterBuilder()
                .gt("montant", 100)
                .build();

        assertEquals(1, filters.size());
        assertEquals("100", filters.get("montant[gt]"));
    }

    @Test
    @DisplayName("gte filter should add greater-or-equal condition")
    void testGteFilter() {
        Map<String, String> filters = new FilterBuilder()
                .gte("montant", 100)
                .build();

        assertEquals(1, filters.size());
        assertEquals("100", filters.get("montant[gte]"));
    }

    @Test
    @DisplayName("lt filter should add less-than condition")
    void testLtFilter() {
        Map<String, String> filters = new FilterBuilder()
                .lt("montant", 1000)
                .build();

        assertEquals(1, filters.size());
        assertEquals("1000", filters.get("montant[lt]"));
    }

    @Test
    @DisplayName("lte filter should add less-or-equal condition")
    void testLteFilter() {
        Map<String, String> filters = new FilterBuilder()
                .lte("montant", 1000)
                .build();

        assertEquals(1, filters.size());
        assertEquals("1000", filters.get("montant[lte]"));
    }

//    @Test
//    @DisplayName("like filter should add pattern matching condition")
//    void testLikeFilter() {
//        Map<String, String> filters = new FilterBuilder()
//                .like("nom", "John%")
//                .build();
//
//        assertEquals(1, filters.size());
//        assertEquals("John%", filters.get("nom[like]"));
//    }

    @Test
    @DisplayName("contains filter should add substring match")
    void testContainsFilter() {
        Map<String, String> filters = new FilterBuilder()
                .contains("type", "CACC")
                .build();

        assertEquals(1, filters.size());
        assertEquals("CACC", filters.get("type[contains]"));
    }

    @Test
    @DisplayName("notContains filter should add negative substring match")
    void testNotContainsFilter() {
        Map<String, String> filters = new FilterBuilder()
                .notContains("type", "SVGS")
                .build();

        assertEquals(1, filters.size());
        assertEquals("SVGS", filters.get("type[notContains]"));
    }

    @Test
    @DisplayName("beginsWith filter should add prefix match")
    void testBeginsWithFilter() {
        Map<String, String> filters = new FilterBuilder()
                .beginsWith("code", "CIC")
                .build();

        assertEquals(1, filters.size());
        assertEquals("CIC", filters.get("code[beginsWith]"));
    }

    @Test
    @DisplayName("endsWith filter should add suffix match")
    void testEndsWithFilter() {
        Map<String, String> filters = new FilterBuilder()
                .endsWith("code", "8282")
                .build();

        assertEquals(1, filters.size());
        assertEquals("8282", filters.get("code[endsWith]"));
    }

    @Test
    @DisplayName("exists filter should add boolean existence condition")
    void testExistsFilter() {
        Map<String, String> filters = new FilterBuilder()
                .exists("statut", true)
                .build();

        assertEquals(1, filters.size());
        assertEquals("true", filters.get("statut[exists]"));
    }

    @Test
    @DisplayName("in filter should add multiple values condition")
    void testInFilter() {
        Map<String, String> filters = new FilterBuilder()
                .in("type", "CACC", "SVGS")
                .build();

        assertEquals(1, filters.size());
        assertEquals("CACC,SVGS", filters.get("type[in]"));
    }

    @Test
    @DisplayName("between filter should add two range conditions")
    void testBetweenFilter() {
        Map<String, String> filters = new FilterBuilder()
                .between("montant", 100, 200)
                .build();

        assertEquals(2, filters.size());
        assertEquals("100", filters.get("montant[gte]"));
        assertEquals("200", filters.get("montant[lte]"));
    }

    @Test
    @DisplayName("custom filter should add user-defined operator")
    void testCustomFilter() {
        Map<String, String> filters = new FilterBuilder()
                .custom("montant", "div", 10)
                .build();

        assertEquals(1, filters.size());
        assertEquals("10", filters.get("montant[div]"));
    }

    @Test
    @DisplayName("where filter should add user-defined operator")
    void testWhereFilter() {
        Map<String, String> filters = new FilterBuilder()
                .where("instructionId", "FACTURES-2025-01-1")
                .build();

        assertEquals(1, filters.size());
        assertEquals("FACTURES-2025-01-1", filters.get("instructionId"));
    }

    @Test
    @DisplayName("chaining filters should combine multiple conditions")
    void testChainingFilters() {
        Map<String, String> filters = new FilterBuilder()
                .eq("a", "1")
                .ne("b", "2")
                .gt("c", 3)
                .gte("d", 4)
                .lt("e", 5)
                .lte("f", 6)
//                .like("g", "pattern%")
                .contains("h", "val")
                .notContains("i", "val2")
                .beginsWith("j", "start")
                .endsWith("k", "end")
                .exists("l", true)
                .in("m", "x", "y")
                .between("n", 10, 20)
                .custom("o", "op", 99)
                .build();
        assertEquals(15, filters.size());
        assertEquals("1", filters.get("a[eq]"));
        assertEquals("2", filters.get("b[ne]"));
        assertEquals("3", filters.get("c[gt]"));
        assertEquals("4", filters.get("d[gte]"));
        assertEquals("5", filters.get("e[lt]"));
        assertEquals("6", filters.get("f[lte]"));
//        assertEquals("pattern%", filters.get("g[like]"));
        assertEquals("val", filters.get("h[contains]"));
        assertEquals("val2", filters.get("i[notContains]"));
        assertEquals("start", filters.get("j[beginsWith]"));
        assertEquals("end", filters.get("k[endsWith]"));
        assertEquals("true", filters.get("l[exists]"));
        assertEquals("x,y", filters.get("m[in]"));
        assertEquals("10", filters.get("n[gte]"));
        assertEquals("20", filters.get("n[lte]"));
        assertEquals("99", filters.get("o[op]"));
    }


    @Test
    @DisplayName("Should handle filters without sort")
    void shouldHandleFiltersWithoutSort() {
        FilterBuilder builder = new FilterBuilder()
                .eq("statut", "ACTIF")
                .gte("solde", "1000");

        Map<String, String> result = builder.build();

        assertThat(result)
                .containsEntry("statut[eq]", "ACTIF")
                .containsEntry("solde[gte]", "1000")
                .doesNotContainKey("sort");
    }

    @Test
    @DisplayName("Should build filters only without sort")
    void shouldBuildFiltersOnlyWithoutSort() {
        FilterBuilder builder = new FilterBuilder()
                .eq("statut", "ACTIF");

        Map<String, String> filtersOnly = builder.buildFiltersOnly();

        assertThat(filtersOnly)
                .containsEntry("statut[eq]", "ACTIF")
                .doesNotContainKey("sort");
    }


    @Test
    @DisplayName("Should clear")
    void shouldClearOnlySort() {
        FilterBuilder builder = new FilterBuilder()
                .eq("statut", "ACTIF")
                .clear();

        Map<String, String> result = builder.build();

        assertThat(result)
                .hasSize(0)
                .doesNotContainKey("sort");
    }

    @Test
    @DisplayName("in filter should do nothing if values are null or empty")
    void testInFilterEmpty() {
        FilterBuilder builder = new FilterBuilder();

        // Test null
        builder.in("field", (Object[]) null);
        // Test vide
        builder.in("field");

        assertThat(builder.build()).isEmpty();
    }

    @Test
    @DisplayName("where filter should ignore null field or null value")
    void testWhereFilterNulls() {
        FilterBuilder builder = new FilterBuilder();

        builder.where(null, "value"); // field null
        builder.where("field", null); // value null

        assertThat(builder.build()).isEmpty();
    }


    @Test
    @DisplayName("add method should ignore null values")
    void testAddNullValue() {
        FilterBuilder builder = new FilterBuilder().eq("test", null);
        assertThat(builder.build()).isEmpty();
    }

}
