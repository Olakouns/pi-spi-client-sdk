package io.github.representation;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.representation.enums.*;
import io.github.util.DataUtil;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class PaiementRepresentation {
    private String txId; 

    private String payeurAlias; 

    private String payeAlias; 

    private BigDecimal montant;

    private String refDocNumero;

    private RefDocType refDocType; 

    private String motif;

    private Boolean programme;

    private Boolean confirmation;

    private String payeNom;

    private String payePays;

    private String payeurNom;

    private String payeurPays;

    private String payeurCompte;

    private String payeCompte;

    private PaiementCategory categorie;

    private PaiementsStatut statut;

    private StatutRaison statutRaison;

    private String end2endId;

    private String instructionId;

    private BigDecimal montantFrais;

    @JsonFormat(pattern = DataUtil.JSON_DATE_FORMAT)
    private OffsetDateTime dateDemande;

    @JsonFormat(pattern = DataUtil.JSON_DATE_FORMAT)
    private OffsetDateTime dateConfirmation;

    @JsonFormat(pattern = DataUtil.JSON_DATE_FORMAT)
    private OffsetDateTime dateEnvoi;

    @JsonFormat(pattern = DataUtil.JSON_DATE_FORMAT)
    private OffsetDateTime dateIrrevocabilite;

    @JsonFormat(pattern = DataUtil.JSON_DATE_FORMAT)
    private OffsetDateTime dateReponse;

    private PaiementRetourStatut retourStatut;

    private StatutRaison retourStatutRaison;

    @JsonFormat(pattern = DataUtil.JSON_DATE_FORMAT)
    private OffsetDateTime retourDateDemande;

    @JsonFormat(pattern = DataUtil.JSON_DATE_FORMAT)
    private OffsetDateTime retourDateIrrevocabilite;

    @JsonFormat(pattern = DataUtil.JSON_DATE_FORMAT)
    private OffsetDateTime retourDateReponse;

    private PaiementAnnulationStatut annulationStatut;

    private PaiementAnnulationMotif annulationMotif;

    private StatutRaison annulationStatutRaison;

    @JsonFormat(pattern = DataUtil.JSON_DATE_FORMAT)
    private OffsetDateTime annulationDateDemande;

    @JsonFormat(pattern = DataUtil.JSON_DATE_FORMAT)
    private OffsetDateTime annulationDateReponse;

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    public String getPayeurAlias() {
        return payeurAlias;
    }

    public void setPayeurAlias(String payeurAlias) {
        this.payeurAlias = payeurAlias;
    }

    public String getPayeAlias() {
        return payeAlias;
    }

    public void setPayeAlias(String payeAlias) {
        this.payeAlias = payeAlias;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public String getRefDocNumero() {
        return refDocNumero;
    }

    public void setRefDocNumero(String refDocNumero) {
        this.refDocNumero = refDocNumero;
    }

    public RefDocType getRefDocType() {
        return refDocType;
    }

    public void setRefDocType(RefDocType refDocType) {
        this.refDocType = refDocType;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public Boolean getProgramme() {
        return programme;
    }

    public void setProgramme(Boolean programme) {
        this.programme = programme;
    }

    public Boolean getConfirmation() {
        return confirmation;
    }

    public void setConfirmation(Boolean confirmation) {
        this.confirmation = confirmation;
    }

    public String getPayeNom() {
        return payeNom;
    }

    public void setPayeNom(String payeNom) {
        this.payeNom = payeNom;
    }

    public String getPayePays() {
        return payePays;
    }

    public void setPayePays(String payePays) {
        this.payePays = payePays;
    }

    public String getPayeurNom() {
        return payeurNom;
    }

    public void setPayeurNom(String payeurNom) {
        this.payeurNom = payeurNom;
    }

    public String getPayeurPays() {
        return payeurPays;
    }

    public void setPayeurPays(String payeurPays) {
        this.payeurPays = payeurPays;
    }

    public String getPayeurCompte() {
        return payeurCompte;
    }

    public void setPayeurCompte(String payeurCompte) {
        this.payeurCompte = payeurCompte;
    }

    public String getPayeCompte() {
        return payeCompte;
    }

    public void setPayeCompte(String payeCompte) {
        this.payeCompte = payeCompte;
    }

    public PaiementCategory getCategorie() {
        return categorie;
    }

    public void setCategorie(PaiementCategory categorie) {
        this.categorie = categorie;
    }

    public PaiementsStatut getStatut() {
        return statut;
    }

    public void setStatut(PaiementsStatut statut) {
        this.statut = statut;
    }

    public StatutRaison getStatutRaison() {
        return statutRaison;
    }

    public void setStatutRaison(StatutRaison statutRaison) {
        this.statutRaison = statutRaison;
    }

    public String getEnd2endId() {
        return end2endId;
    }

    public void setEnd2endId(String end2endId) {
        this.end2endId = end2endId;
    }

    public String getInstructionId() {
        return instructionId;
    }

    public void setInstructionId(String instructionId) {
        this.instructionId = instructionId;
    }

    public BigDecimal getMontantFrais() {
        return montantFrais;
    }

    public void setMontantFrais(BigDecimal montantFrais) {
        this.montantFrais = montantFrais;
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

    public OffsetDateTime getDateEnvoi() {
        return dateEnvoi;
    }

    public void setDateEnvoi(OffsetDateTime dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    public OffsetDateTime getDateIrrevocabilite() {
        return dateIrrevocabilite;
    }

    public void setDateIrrevocabilite(OffsetDateTime dateIrrevocabilite) {
        this.dateIrrevocabilite = dateIrrevocabilite;
    }

    public OffsetDateTime getDateReponse() {
        return dateReponse;
    }

    public void setDateReponse(OffsetDateTime dateReponse) {
        this.dateReponse = dateReponse;
    }

    public PaiementRetourStatut getRetourStatut() {
        return retourStatut;
    }

    public void setRetourStatut(PaiementRetourStatut retourStatut) {
        this.retourStatut = retourStatut;
    }

    public StatutRaison getRetourStatutRaison() {
        return retourStatutRaison;
    }

    public void setRetourStatutRaison(StatutRaison retourStatutRaison) {
        this.retourStatutRaison = retourStatutRaison;
    }

    public OffsetDateTime getRetourDateDemande() {
        return retourDateDemande;
    }

    public void setRetourDateDemande(OffsetDateTime retourDateDemande) {
        this.retourDateDemande = retourDateDemande;
    }

    public OffsetDateTime getRetourDateIrrevocabilite() {
        return retourDateIrrevocabilite;
    }

    public void setRetourDateIrrevocabilite(OffsetDateTime retourDateIrrevocabilite) {
        this.retourDateIrrevocabilite = retourDateIrrevocabilite;
    }

    public OffsetDateTime getRetourDateReponse() {
        return retourDateReponse;
    }

    public void setRetourDateReponse(OffsetDateTime retourDateReponse) {
        this.retourDateReponse = retourDateReponse;
    }

    public PaiementAnnulationStatut getAnnulationStatut() {
        return annulationStatut;
    }

    public void setAnnulationStatut(PaiementAnnulationStatut annulationStatut) {
        this.annulationStatut = annulationStatut;
    }

    public PaiementAnnulationMotif getAnnulationMotif() {
        return annulationMotif;
    }

    public void setAnnulationMotif(PaiementAnnulationMotif annulationMotif) {
        this.annulationMotif = annulationMotif;
    }

    public StatutRaison getAnnulationStatutRaison() {
        return annulationStatutRaison;
    }

    public void setAnnulationStatutRaison(StatutRaison annulationStatutRaison) {
        this.annulationStatutRaison = annulationStatutRaison;
    }

    public OffsetDateTime getAnnulationDateDemande() {
        return annulationDateDemande;
    }

    public void setAnnulationDateDemande(OffsetDateTime annulationDateDemande) {
        this.annulationDateDemande = annulationDateDemande;
    }

    public OffsetDateTime getAnnulationDateReponse() {
        return annulationDateReponse;
    }

    public void setAnnulationDateReponse(OffsetDateTime annulationDateReponse) {
        this.annulationDateReponse = annulationDateReponse;
    }

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder {
        private final PaiementRepresentation instance = new PaiementRepresentation();

        public Builder txId(String txId) {
            this.instance.setTxId(txId);
            return this;
        }

        public Builder payeurAlias(String payeurAlias) {
            this.instance.setPayeurAlias(payeurAlias);
            return this;
        }

        public Builder payeAlias(String payeAlias) {
            this.instance.setPayeAlias(payeAlias);
            return this;
        }

        public Builder montant(BigDecimal montant) {
            this.instance.setMontant(montant);
            return this;
        }

        public Builder setRefDocNumero(String refDocNumero) {
            this.instance.refDocNumero = refDocNumero;
            return this;
        }

        public Builder refDocType(RefDocType refDocType) {
            this.instance.setRefDocType(refDocType);
            return this;
        }

        public Builder motif(String motif) {
            this.instance.setMotif(motif);
            return this;
        }

        public Builder programme(Boolean programme) {
            this.instance.setProgramme(programme);
            return this;
        }

        public Builder confirmation(Boolean confirmation) {
            this.instance.setConfirmation(confirmation);
            return this;
        }

        public Builder payeNom(String payeNom) {
            this.instance.setPayeNom(payeNom);
            return this;
        }


        public Builder payePays(String payePays) {
            this.instance.setPayePays(payePays);
            return this;
        }

        public Builder payeurNom(String payeurNom) {
            this.instance.setPayeurNom(payeurNom);
            return this;
        }

        public Builder payeurPays(String payeurPays) {
            this.instance.setPayeurPays(payeurPays);
            return this;
        }

        public Builder payeurCompte(String payeurCompte) {
            this.instance.setPayeurCompte(payeurCompte);
            return this;
        }

        public Builder payeCompte(String payeCompte) {
            this.instance.setPayeCompte(payeCompte);
            return this;
        }

        public Builder categorie(PaiementCategory categorie) {
            this.instance.setCategorie(categorie);
            return this;
        }

        public Builder statut(PaiementsStatut statut) {
            this.instance.setStatut(statut);
            return this;
        }

        public Builder statutRaison(StatutRaison statutRaison) {
            this.instance.setStatutRaison(statutRaison);
            return this;
        }

        public Builder end2endId(String end2endId) {
            this.instance.setEnd2endId(end2endId);
            return this;
        }

        public Builder instructionId(String instructionId) {
            this.instance.setInstructionId(instructionId);
            return this;
        }

        public Builder montantFrais(BigDecimal montantFrais) {
            this.instance.setMontantFrais(montantFrais);
            return this;
        }

        public Builder dateDemande(OffsetDateTime dateDemande) {
            this.instance.setDateDemande(dateDemande);
            return this;
        }

        public Builder dateConfirmation(OffsetDateTime dateConfirmation) {
            this.instance.setDateConfirmation(dateConfirmation);
            return this;
        }

        public Builder dateEnvoi(OffsetDateTime dateEnvoi) {
            this.instance.setDateEnvoi(dateEnvoi);
            return this;
        }

        public Builder dateIrrevocabilite(OffsetDateTime dateIrrevocabilite) {
            this.instance.setDateIrrevocabilite(dateIrrevocabilite);
            return this;
        }

        public Builder dateReponse(OffsetDateTime dateReponse) {
            this.instance.setDateReponse(dateReponse);
            return this;
        }

        public Builder retourStatut(PaiementRetourStatut retourStatut) {
            this.instance.setRetourStatut(retourStatut);
            return this;
        }

        public Builder retourStatutRaison(StatutRaison retourStatutRaison) {
            this.instance.setRetourStatutRaison(retourStatutRaison);
            return this;
        }

        public Builder retourDateDemande(OffsetDateTime retourDateDemande) {
            this.instance.setRetourDateDemande(retourDateDemande);
            return this;
        }

        public Builder retourDateIrrevocabilite(OffsetDateTime retourDateIrrevocabilite) {
            this.instance.setRetourDateIrrevocabilite(retourDateIrrevocabilite);
            return this;
        }

        public Builder retourDateReponse(OffsetDateTime retourDateReponse) {
            this.instance.setRetourDateReponse(retourDateReponse);
            return this;
        }

        public Builder annulationStatut(PaiementAnnulationStatut annulationStatut) {
            this.instance.setAnnulationStatut(annulationStatut);
            return this;
        }

        public Builder annulationMotif(PaiementAnnulationMotif annulationMotif) {
            this.instance.setAnnulationMotif(annulationMotif);
            return this;
        }

        public Builder annulationStatutRaison(StatutRaison annulationStatutRaison) {
            this.instance.setAnnulationStatutRaison(annulationStatutRaison);
            return this;
        }

        public Builder annulationDateDemande(OffsetDateTime annulationDateDemande) {
            this.instance.setAnnulationDateDemande(annulationDateDemande);
            return this;
        }

        public Builder annulationDateReponse(OffsetDateTime annulationDateReponse) {
            this.instance.setAnnulationDateReponse(annulationDateReponse);
            return this;
        }
        public PaiementRepresentation build() {
            return this.instance;
        }
    }
}
