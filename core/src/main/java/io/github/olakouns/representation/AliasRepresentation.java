package io.github.olakouns.representation;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.olakouns.representation.enums.AliasType;
import io.github.olakouns.util.DataUtil;

import java.time.OffsetDateTime;

public class AliasRepresentation {
    private String cle;
    private AliasType type;
    private String compte;

    @JsonFormat(pattern = DataUtil.JSON_DATE_FORMAT)
    private OffsetDateTime dateCreation;

    @JsonFormat(pattern = DataUtil.JSON_DATE_FORMAT)
    private OffsetDateTime dateModification;

    public String getCle() {
        return cle;
    }

    public void setCle(String cle) {
        this.cle = cle;
    }

    public AliasType getType() {
        return type;
    }

    public void setType(AliasType type) {
        this.type = type;
    }

    public String getCompte() {
        return compte;
    }

    public void setCompte(String compte) {
        this.compte = compte;
    }

    public OffsetDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(OffsetDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public OffsetDateTime getDateModification() {
        return dateModification;
    }

    public void setDateModification(OffsetDateTime dateModification) {
        this.dateModification = dateModification;
    }
}
