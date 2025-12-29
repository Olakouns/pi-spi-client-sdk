package io.github.razacki.representation;

import java.util.List;

public class PaiementGroupeRequest {
    private String payeurAlias;
    private String instructionId;
    private Boolean confirmation;
    private String motif;
    private List<PaiementRepresentation> transactions;


    public String getPayeurAlias() {
        return payeurAlias;
    }

    public void setPayeurAlias(String payeurAlias) {
        this.payeurAlias = payeurAlias;
    }

    public String getInstructionId() {
        return instructionId;
    }

    public void setInstructionId(String instructionId) {
        this.instructionId = instructionId;
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

    public List<PaiementRepresentation> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<PaiementRepresentation> transactions) {
        this.transactions = transactions;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final PaiementGroupeRequest instance;

        private Builder() {
            this.instance = new PaiementGroupeRequest();
        }

        public Builder instructionId(String instructionId) {
            instance.setInstructionId(instructionId);
            return this;
        }

        public Builder payeurAlias(String payeurAlias) {
            instance.setPayeurAlias(payeurAlias);
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

        public Builder transactions(List<PaiementRepresentation> transactions) {
            instance.setTransactions(transactions);
            return this;
        }

        public PaiementGroupeRequest build() {
            return instance;
        }
    }
}
