package io.github.razacki.representation;

import io.github.razacki.representation.enums.StatutRaison;

public class ConfirmationRequest {
    private Boolean decision;
    private StatutRaison raison;

    public ConfirmationRequest(Boolean decision) {
        this.decision = decision;
    }

    public ConfirmationRequest() {
    }

    public Boolean getDecision() {
        return decision;
    }

    public void setDecision(Boolean decision) {
        this.decision = decision;
    }

    public StatutRaison getRaison() {
        return raison;
    }

    public void setRaison(StatutRaison raison) {
        this.raison = raison;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        final ConfirmationRequest instance = new ConfirmationRequest();

        public Builder decision(Boolean decision) {
            instance.setDecision(decision);
            return this;
        }

        public Builder raison(StatutRaison raison) {
            instance.setRaison(raison);
            return this;
        }

        public ConfirmationRequest build() {
            return instance;
        }
    }
}
