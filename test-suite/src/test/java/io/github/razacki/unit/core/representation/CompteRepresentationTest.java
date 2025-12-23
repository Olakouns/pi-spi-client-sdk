package io.github.razacki.unit.core.representation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.representation.CompteRepresentation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.time.OffsetDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Tag("unit")
public class CompteRepresentationTest {

    private static ObjectMapper objectMapper;

    @BeforeAll
    static void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void testDeserializationFromJson() throws Exception {
        InputStream is = getClass().getClassLoader()
                .getResourceAsStream("unit/mock-responses/compte-response.json");

        CompteRepresentation compte = objectMapper.readValue(is, CompteRepresentation.class);

        assertThat(compte).isNotNull();
        assertThat(compte.getNumero()).isEqualTo("CIC2344256727788288822");
        assertThat(compte.getType()).isEqualTo("CACC");
        assertThat(compte.getStatut()).isEqualTo("OUVERT");
        assertThat(compte.getDateOuverture()).isEqualTo(OffsetDateTime.parse("2023-02-21T15:30:01.250Z"));
    }

    @Test
    void testSerializationToJson() throws Exception {
        CompteRepresentation compte = new CompteRepresentation();
        compte.setNumero("CIC2344256727788288822");
        compte.setType("CACC");
        compte.setStatut("OUVERT");
        compte.setDateOuverture(OffsetDateTime.parse("2023-02-21T15:30:01.250Z"));

        String json = objectMapper.writeValueAsString(compte);

        assertThat(json).contains("\"numero\":\"CIC2344256727788288822\"");
        assertThat(json).contains("\"type\":\"CACC\"");
        assertThat(json).contains("\"statut\":\"OUVERT\"");
        assertThat(json).contains("\"dateOuverture\":\"2023-02-21T15:30:01.250Z\"");
    }

    @Test
    void testRoundTripSerialization() throws Exception {
        CompteRepresentation original = new CompteRepresentation();
        original.setNumero("CIC2344256727788288822");
        original.setType("CACC");
        original.setStatut("OUVERT");
        original.setDateOuverture(OffsetDateTime.parse("2023-02-21T15:30:01.250Z"));

        String json = objectMapper.writeValueAsString(original);

        CompteRepresentation deserialized = objectMapper.readValue(json, CompteRepresentation.class);
        assertThat(deserialized.getNumero()).isEqualTo(original.getNumero());
        assertThat(deserialized.getType()).isEqualTo(original.getType());
        assertThat(deserialized.getStatut()).isEqualTo(original.getStatut());
        assertThat(deserialized.getDateOuverture()).isEqualTo(original.getDateOuverture());
    }
}
