package io.github.razacki.representation;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.razacki.representation.enums.CompteStatut;
import io.github.razacki.representation.enums.CompteType;
import io.github.razacki.util.DataUtil;

import java.time.OffsetDateTime;

public class CompteRepresentation {
    private String numero;
    private CompteType type;
    @JsonFormat(pattern = DataUtil.JSON_DATE_FORMAT)
    private OffsetDateTime dateOuverture;
    private CompteStatut statut;

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public CompteType getType() {
        return type;
    }

    public void setType(CompteType type) {
        this.type = type;
    }

    public OffsetDateTime getDateOuverture() {
        return dateOuverture;
    }

    public void setDateOuverture(OffsetDateTime dateOuverture) {
        this.dateOuverture = dateOuverture;
    }

    public CompteStatut getStatut() {
        return statut;
    }

    public void setStatut(CompteStatut statut) {
        this.statut = statut;
    }
}
