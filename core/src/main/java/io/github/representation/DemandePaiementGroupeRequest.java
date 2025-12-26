package io.github.representation;

import java.util.List;

public class DemandePaiementGroupeRequest {
    private String payeAlias;
    private String instructionId;
    private Boolean confirmation;
    private String motif;
    private List<DemandesPaiementsRepresentation> transactions;

    public String getPayeAlias() {
        return payeAlias;
    }

    public void setPayeAlias(String payeAlias) {
        this.payeAlias = payeAlias;
    }

    public Boolean getConfirmation() {
        return confirmation;
    }

    public void setConfirmation(Boolean confirmation) {
        this.confirmation = confirmation;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public List<DemandesPaiementsRepresentation> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<DemandesPaiementsRepresentation> transactions) {
        this.transactions = transactions;
    }

    public String getInstructionId() {
        return instructionId;
    }

    public void setInstructionId(String instructionId) {
        this.instructionId = instructionId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final DemandePaiementGroupeRequest instance;

        private Builder() {
            this.instance = new DemandePaiementGroupeRequest();
        }

        public Builder instructionId(String instructionId) {
            instance.setInstructionId(instructionId);
            return this;
        }

        public Builder payeAlias(String payeAlias) {
            instance.setPayeAlias(payeAlias);
            return this;
        }

        public Builder confirmation(Boolean confirmation) {
            instance.setConfirmation(confirmation);
            return this;
        }

        public Builder motif(String motif) {
            instance.setMotif(motif);
            return this;
        }

        public Builder transactions(List<DemandesPaiementsRepresentation> transactions) {
            instance.setTransactions(transactions);
            return this;
        }

        public DemandePaiementGroupeRequest build() {
            return instance;
        }
    }

}
