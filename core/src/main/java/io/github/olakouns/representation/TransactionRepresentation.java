package io.github.olakouns.representation;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.olakouns.representation.enums.TransactionStatut;
import io.github.olakouns.representation.enums.TransactionStatutRaison;
import io.github.olakouns.util.DataUtil;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class TransactionRepresentation {
    private String txId;
    private String payeurNumero;
    private String payeNumero;
    private String motif;
    private BigDecimal montant;
    private TransactionStatut statut;
    private TransactionStatutRaison statutRaison;
    @JsonFormat(pattern = DataUtil.JSON_DATE_FORMAT)
    private OffsetDateTime dateEnvoi;
    @JsonFormat(pattern = DataUtil.JSON_DATE_FORMAT)
    private OffsetDateTime dateIrrevocabilite;
    private String end2endId;

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    public String getPayeurNumero() {
        return payeurNumero;
    }

    public void setPayeurNumero(String payeurNumero) {
        this.payeurNumero = payeurNumero;
    }

    public String getPayeNumero() {
        return payeNumero;
    }

    public void setPayeNumero(String payeNumero) {
        this.payeNumero = payeNumero;
    }

    public TransactionStatut getStatut() {
        return statut;
    }

    public void setStatut(TransactionStatut statut) {
        this.statut = statut;
    }

    public OffsetDateTime getDateEnvoi() {
        return dateEnvoi;
    }

    public void setDateEnvoi(OffsetDateTime dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    public String getEnd2endId() {
        return end2endId;
    }

    public void setEnd2endId(String end2endId) {
        this.end2endId = end2endId;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public TransactionStatutRaison getStatutRaison() {
        return statutRaison;
    }

    public void setStatutRaison(TransactionStatutRaison statutRaison) {
        this.statutRaison = statutRaison;
    }

    public OffsetDateTime getDateIrrevocabilite() {
        return dateIrrevocabilite;
    }

    public void setDateIrrevocabilite(OffsetDateTime dateIrrevocabilite) {
        this.dateIrrevocabilite = dateIrrevocabilite;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final TransactionRepresentation representation = new TransactionRepresentation();

        public Builder txId(String txId) {
            representation.setTxId(txId);
            return this;
        }

        public Builder payeurNumero(String payeurNumero) {
            representation.setPayeurNumero(payeurNumero);
            return this;
        }

        public Builder payeNumero(String payeNumero) {
            representation.setPayeNumero(payeNumero);
            return this;
        }

        public Builder motif(String motif) {
            representation.setMotif(motif);
            return this;
        }

        public Builder montant(BigDecimal montant) {
            representation.setMontant(montant);
            return this;
        }

        public Builder statut(TransactionStatut statut) {
            representation.setStatut(statut);
            return this;
        }

        public Builder statutRaison(TransactionStatutRaison statutRaison) {
            representation.setStatutRaison(statutRaison);
            return this;
        }

        public Builder dateEnvoi(OffsetDateTime dateEnvoi) {
            representation.setDateEnvoi(dateEnvoi);
            return this;
        }

        public Builder dateIrrevocabilite(OffsetDateTime dateIrrevocabilite) {
            representation.setDateIrrevocabilite(dateIrrevocabilite);
            return this;
        }

        public Builder end2endId(String end2endId) {
            representation.setEnd2endId(end2endId);
            return this;
        }

        public TransactionRepresentation build() {
            return representation;
        }
    }
}
