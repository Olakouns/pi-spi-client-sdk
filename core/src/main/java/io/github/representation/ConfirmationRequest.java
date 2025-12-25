package io.github.representation;

public class ConfirmationRequest {
    private Boolean decision;

    public ConfirmationRequest(Boolean decision) {
        this.decision = decision;
    }

    public Boolean getDecision() {
        return decision;
    }

    public void setDecision(Boolean decision) {
        this.decision = decision;
    }
}
