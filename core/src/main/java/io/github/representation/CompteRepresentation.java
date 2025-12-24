package io.github.representation;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.util.DataUtil;

import java.time.OffsetDateTime;

public class CompteRepresentation {
    private String numero;
    private String type;
    @JsonFormat(pattern = DataUtil.JSON_DATE_FORMAT)
    private OffsetDateTime dateOuverture;
    private String statut;

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public OffsetDateTime getDateOuverture() {
        return dateOuverture;
    }

    public void setDateOuverture(OffsetDateTime dateOuverture) {
        this.dateOuverture = dateOuverture;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }
}
