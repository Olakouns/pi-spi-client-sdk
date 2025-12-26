package io.github.razacki.unit.core.representation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.representation.CompteRepresentation;
import io.github.representation.PagedResponse;
import io.github.representation.enums.CompteStatut;
import io.github.representation.enums.CompteType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@Tag("unit")
public class PagedResponseCompteTest {
    private static ObjectMapper objectMapper;

    @BeforeAll
    static void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void testDeserializationPagedResponseWithCompteRepresentation() throws Exception {
        InputStream is = getClass().getClassLoader()
                .getResourceAsStream("unit/mock-responses/paged-compte-response.json");

        PagedResponse<CompteRepresentation> response  = objectMapper.readValue(is,
                objectMapper.getTypeFactory().constructParametricType(PagedResponse.class, CompteRepresentation.class)
        );

        List<CompteRepresentation> data = response.getData();
        assertThat(data).hasSize(3);

        assertThat(data.get(0).getNumero()).isEqualTo("CIC2344256727788288822");
        assertThat(data.get(0).getType()).isEqualTo(CompteType.CACC);
        assertThat(data.get(0).getStatut()).isEqualTo(CompteStatut.OUVERT);
        assertThat(data.get(0).getDateOuverture()).isEqualTo(OffsetDateTime.parse("2023-02-21T15:30:01.250Z"));

        assertThat(data.get(1).getNumero()).isEqualTo("CIC2344256727788288833");
        assertThat(data.get(1).getType()).isEqualTo(CompteType.SVGS);
        assertThat(data.get(1).getStatut()).isEqualTo(CompteStatut.OUVERT);
        assertThat(data.get(1).getDateOuverture()).isEqualTo(OffsetDateTime.parse("2023-03-15T10:20:30.000Z"));

        assertThat(data.get(2).getNumero()).isEqualTo("CIC2344256727788288844");
        assertThat(data.get(2).getType()).isEqualTo(CompteType.CACC);
        assertThat(data.get(2).getStatut()).isEqualTo(CompteStatut.BLOQUE);
        assertThat(data.get(2).getDateOuverture()).isEqualTo(OffsetDateTime.parse("2023-01-10T08:45:00.000Z"));

        PagedResponse.Meta meta = response.getMeta();
        assertThat(meta).isNotNull();
        assertThat(meta.getTotal()).isEqualTo(3);
        assertThat(meta.getSize()).isEqualTo(3);
        assertThat(meta.getPage()).isEqualTo("1");
        assertThat(meta.getNext()).isNull();
        assertThat(meta.getPrev()).isNull();
    }
}
