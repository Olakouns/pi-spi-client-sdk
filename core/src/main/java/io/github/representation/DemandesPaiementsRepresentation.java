package io.github.representation;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.representation.enums.PaiementCategory;
import io.github.representation.enums.PaiementsStatut;
import io.github.representation.enums.StatutRaison;
import io.github.util.DataUtil;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class DemandesPaiementsRepresentation {
    private String txId;
    private PaiementCategory categorie;
    private String payeAlias;
    private String logoUrl;
    private String payeurAlias;
    private Boolean confirmation;
    private RemiseRepresentation remise;

    private Boolean debitDiffere;
    private BigDecimal montant;
    private BigDecimal montantAchat;
    private BigDecimal montantRetrait;
    private BigDecimal montantFrais;

    private String motif;

    private String refDocNumero;
    private RefDocType refDocType;

    private PaiementsStatut statut;
    private StatutRaison statutRaison;

    private String end2endId;

    private String payeurNom;
    private String payeurPays;

    private String payeNom;
    private String payePays;

    @JsonFormat(pattern = DataUtil.JSON_DATE_FORMAT)
    private OffsetDateTime dateLimitePaiement;

    @JsonFormat(pattern = DataUtil.JSON_DATE_FORMAT)
    private OffsetDateTime dateLimiteReponse;

    @JsonFormat(pattern = DataUtil.JSON_DATE_FORMAT)
    private OffsetDateTime dateDemande;

    @JsonFormat(pattern = DataUtil.JSON_DATE_FORMAT)
    private OffsetDateTime dateConfirmation;

    @JsonFormat(pattern = DataUtil.JSON_DATE_FORMAT)
    private OffsetDateTime dateEnvoi;

    @JsonFormat(pattern = DataUtil.JSON_DATE_FORMAT)
    private OffsetDateTime dateReponse;

    @JsonFormat(pattern = DataUtil.JSON_DATE_FORMAT)
    private OffsetDateTime dateIrrevocabilite;

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    public PaiementCategory getCategorie() {
        return categorie;
    }

    public void setCategorie(PaiementCategory categorie) {
        this.categorie = categorie;
    }

    public String getPayeAlias() {
        return payeAlias;
    }

    public void setPayeAlias(String payeAlias) {
        this.payeAlias = payeAlias;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getPayeurAlias() {
        return payeurAlias;
    }

    public void setPayeurAlias(String payeurAlias) {
        this.payeurAlias = payeurAlias;
    }

    public Boolean getConfirmation() {
        return confirmation;
    }

    public void setConfirmation(Boolean confirmation) {
        this.confirmation = confirmation;
    }

    public RemiseRepresentation getRemise() {
        return remise;
    }

    public void setRemise(RemiseRepresentation remise) {
        this.remise = remise;
    }

    public Boolean getDebitDiffere() {
        return debitDiffere;
    }

    public void setDebitDiffere(Boolean debitDiffere) {
        this.debitDiffere = debitDiffere;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public BigDecimal getMontantAchat() {
        return montantAchat;
    }

    public void setMontantAchat(BigDecimal montantAchat) {
        this.montantAchat = montantAchat;
    }

    public BigDecimal getMontantRetrait() {
        return montantRetrait;
    }

    public void setMontantRetrait(BigDecimal montantRetrait) {
        this.montantRetrait = montantRetrait;
    }

    public BigDecimal getMontantFrais() {
        return montantFrais;
    }

    public void setMontantFrais(BigDecimal montantFrais) {
        this.montantFrais = montantFrais;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
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

    public OffsetDateTime getDateLimitePaiement() {
        return dateLimitePaiement;
    }

    public void setDateLimitePaiement(OffsetDateTime dateLimitePaiement) {
        this.dateLimitePaiement = dateLimitePaiement;
    }

    public OffsetDateTime getDateLimiteReponse() {
        return dateLimiteReponse;
    }

    public void setDateLimiteReponse(OffsetDateTime dateLimiteReponse) {
        this.dateLimiteReponse = dateLimiteReponse;
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

    public OffsetDateTime getDateReponse() {
        return dateReponse;
    }

    public void setDateReponse(OffsetDateTime dateReponse) {
        this.dateReponse = dateReponse;
    }

    public OffsetDateTime getDateIrrevocabilite() {
        return dateIrrevocabilite;
    }

    public void setDateIrrevocabilite(OffsetDateTime dateIrrevocabilite) {
        this.dateIrrevocabilite = dateIrrevocabilite;
    }


    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final DemandesPaiementsRepresentation representation = new DemandesPaiementsRepresentation();

        public Builder txId(String txId) {
            representation.setTxId(txId);
            return this;
        }

        public Builder categorie(PaiementCategory categorie) {
            representation.setCategorie(categorie);
            return this;
        }

        public Builder payeAlias(String payeAlias) {
            representation.setPayeAlias(payeAlias);
            return this;
        }

        public Builder logoUrl(String logoUrl) {
            representation.setLogoUrl(logoUrl);
            return this;
        }

        public Builder payeurAlias(String payeurAlias) {
            representation.setPayeurAlias(payeurAlias);
            return this;
        }

        public Builder confirmation(Boolean confirmation) {
            representation.setConfirmation(confirmation);
            return this;
        }

        public Builder remise(RemiseRepresentation remise) {
            representation.setRemise(remise);
            return this;
        }

        public Builder debitDiffere(Boolean debitDiffere) {
            representation.setDebitDiffere(debitDiffere);
            return this;
        }

        public Builder montant(BigDecimal montant) {
            representation.setMontant(montant);
            return this;
        }

        public Builder montantAchat(BigDecimal montantAchat) {
            representation.setMontantAchat(montantAchat);
            return this;
        }

        public Builder montantRetrait(BigDecimal montantRetrait) {
            representation.setMontantRetrait(montantRetrait);
            return this;
        }

        public Builder montantFrais(BigDecimal montantFrais) {
            representation.setMontantFrais(montantFrais);
            return this;
        }

        public Builder motif(String motif) {
            representation.setMotif(motif);
            return this;
        }

        public Builder refDocNumero(String refDocNumero) {
            representation.setRefDocNumero(refDocNumero);
            return this;
        }

        public Builder refDocType(RefDocType refDocType) {
            representation.setRefDocType(refDocType);
            return this;
        }

        public Builder statut(PaiementsStatut statut) {
            representation.setStatut(statut);
            return this;
        }

        public Builder statutRaison(StatutRaison statutRaison) {
            representation.setStatutRaison(statutRaison);
            return this;
        }

        public Builder end2endId(String end2endId) {
            representation.setEnd2endId(end2endId);
            return this;
        }

        public Builder payeurNom(String payeurNom) {
            representation.setPayeurNom(payeurNom);
            return this;
        }

        public Builder payeurPays(String payeurPays) {
            representation.setPayeurPays(payeurPays);
            return this;
        }

        public Builder payeNom(String payeNom) {
            representation.setPayeNom(payeNom);
            return this;
        }

        public Builder payePays(String payePays) {
            representation.setPayePays(payePays);
            return this;
        }

        public Builder dateLimitePaiement(OffsetDateTime dateLimitePaiement) {
            representation.setDateLimitePaiement(dateLimitePaiement);
            return this;
        }

        public Builder dateLimiteReponse(OffsetDateTime dateLimiteReponse) {
            representation.setDateLimiteReponse(dateLimiteReponse);
            return this;
        }

        public Builder dateDemande(OffsetDateTime dateDemande) {
            representation.setDateDemande(dateDemande);
            return this;
        }

        public Builder dateConfirmation(OffsetDateTime dateConfirmation) {
            representation.setDateConfirmation(dateConfirmation);
            return this;
        }

        public Builder dateEnvoi(OffsetDateTime dateEnvoi) {
            representation.setDateEnvoi(dateEnvoi);
            return this;
        }

        public Builder dateReponse(OffsetDateTime dateReponse) {
            representation.setDateReponse(dateReponse);
            return this;
        }

        public Builder dateIrrevocabilite(OffsetDateTime dateIrrevocabilite) {
            representation.setDateIrrevocabilite(dateIrrevocabilite);
            return this;
        }

        public DemandesPaiementsRepresentation build() {
            return representation;
        }
    }
}
