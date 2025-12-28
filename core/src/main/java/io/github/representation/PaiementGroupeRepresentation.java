package io.github.representation;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.representation.enums.DemandePaiementGroupeStatut;
import io.github.util.DataUtil;

import java.time.OffsetDateTime;

public class PaiementGroupeRepresentation {
    private String instructionId;

    private DemandePaiementGroupeStatut statut;

    @JsonFormat(pattern = DataUtil.JSON_DATE_FORMAT)
    private OffsetDateTime dateDemande;

    @JsonFormat(pattern = DataUtil.JSON_DATE_FORMAT)
    private OffsetDateTime dateConfirmation;

    @JsonFormat(pattern = DataUtil.JSON_DATE_FORMAT)
    private OffsetDateTime dateExpiration;

    @JsonFormat(pattern = DataUtil.JSON_DATE_FORMAT)
    private OffsetDateTime dateAnnulation;

    private Integer transactionsTotal;

    private Integer transactionsInitiees;

    private Integer transactionsEnvoyees;

    private Integer transactionsIrrevocables;

    private Integer transactionsRejetees;


    public String getInstructionId() {
        return instructionId;
    }

    public void setInstructionId(String instructionId) {
        this.instructionId = instructionId;
    }

    public DemandePaiementGroupeStatut getStatut() {
        return statut;
    }

    public void setStatut(DemandePaiementGroupeStatut statut) {
        this.statut = statut;
    }

    public OffsetDateTime getDateDemande() {
        return dateDemande;
    }

    public void setDateDemande(OffsetDateTime dateDemande) {
        this.dateDemande = dateDemande;
    }

    public OffsetDateTime getDateConfirmation() {
        return dateConfirmation;
    }

    public void setDateConfirmation(OffsetDateTime dateConfirmation) {
        this.dateConfirmation = dateConfirmation;
    }

    public OffsetDateTime getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(OffsetDateTime dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public Integer getTransactionsTotal() {
        return transactionsTotal;
    }

    public void setTransactionsTotal(Integer transactionsTotal) {
        this.transactionsTotal = transactionsTotal;
    }

    public Integer getTransactionsInitiees() {
        return transactionsInitiees;
    }

    public void setTransactionsInitiees(Integer transactionsInitiees) {
        this.transactionsInitiees = transactionsInitiees;
    }

    public Integer getTransactionsEnvoyees() {
        return transactionsEnvoyees;
    }

    public void setTransactionsEnvoyees(Integer transactionsEnvoyees) {
        this.transactionsEnvoyees = transactionsEnvoyees;
    }

    public Integer getTransactionsIrrevocables() {
        return transactionsIrrevocables;
    }

    public void setTransactionsIrrevocables(Integer transactionsIrrevocables) {
        this.transactionsIrrevocables = transactionsIrrevocables;
    }

    public Integer getTransactionsRejetees() {
        return transactionsRejetees;
    }

    public void setTransactionsRejetees(Integer transactionsRejetees) {
        this.transactionsRejetees = transactionsRejetees;
    }

    public OffsetDateTime getDateAnnulation() {
        return dateAnnulation;
    }

    public void setDateAnnulation(OffsetDateTime dateAnnulation) {
        this.dateAnnulation = dateAnnulation;
    }
}
