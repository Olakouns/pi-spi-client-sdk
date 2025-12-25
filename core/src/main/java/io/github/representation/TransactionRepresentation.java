package io.github.representation;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.representation.enums.TransactionRepresentationStatut;
import io.github.representation.enums.TransactionStatutRaison;
import io.github.util.DataUtil;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class TransactionRepresentation {
    private String txId;
    private String payeurNumero;
    private String payeNumero;
    private BigDecimal montant;
    private TransactionRepresentationStatut statut;
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

    public TransactionRepresentationStatut getStatut() {
        return statut;
    }

    public void setStatut(TransactionRepresentationStatut statut) {
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
}
