package io.github.representation;

import io.github.representation.enums.PaiementAnnulationMotif;

public class CancelPaymentRequest {
    private PaiementAnnulationMotif raison;

    public CancelPaymentRequest(PaiementAnnulationMotif raison) {
        this.raison = raison;
    }

    public PaiementAnnulationMotif getRaison() {
        return raison;
    }

    public void setRaison(PaiementAnnulationMotif raison) {
        this.raison = raison;
    }
}
