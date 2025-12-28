package io.github.resource.wrapper;

import io.github.representation.CancelPaymentRequest;
import io.github.representation.ConfirmationRequest;
import io.github.representation.PagedResponse;
import io.github.representation.PaiementRepresentation;
import io.github.representation.enums.PaiementAnnulationMotif;
import io.github.resource.PaiementResource;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;

public class PaiementResourceWrapper extends ListableWrapper<PaiementRepresentation, PaiementResource> {
    private static final String PAIEMENT_TX_ID = "PaiementRepresentation txId";
    private static final String PAIEMENT_END_2_END_ID = "PaiementRepresentation end2endId";

    public PaiementResourceWrapper(PaiementResource proxy, WebTarget target) {
        super(proxy, target, new GenericType<PagedResponse<PaiementRepresentation>>() {
        });
    }

    public PaiementRepresentation create(PaiementRepresentation paiementRepresentation) {
        validateNotNull(paiementRepresentation, "paiementRepresentation");
        return proxy.create(paiementRepresentation);
    }

    public PaiementRepresentation findById(String txId) {
        validateNotEmpty(txId, PAIEMENT_TX_ID);
        return proxy.findById(txId);
    }

    public PaiementRepresentation confirm(String txId, boolean decision) {
        validateNotEmpty(txId, PAIEMENT_TX_ID);
        return proxy.confirm(txId, new ConfirmationRequest(decision));
    }

    public PaiementRepresentation getStatus(String end2endId) {
        validateNotEmpty(end2endId, PAIEMENT_END_2_END_ID);
        return proxy.getStatus(end2endId);
    }

    public PaiementRepresentation returnFunds(String end2endId) {
        validateNotEmpty(end2endId, PAIEMENT_END_2_END_ID);
        return proxy.returnFunds(end2endId);
    }

    public PaiementRepresentation requestCancellation(String end2endId, PaiementAnnulationMotif motif) {
        validateNotEmpty(end2endId, PAIEMENT_END_2_END_ID);
        return proxy.requestCancellation(end2endId, new CancelPaymentRequest(motif));
    }

    public PaiementRepresentation respondToCancellation(String end2endId, boolean decision) {
        validateNotEmpty(end2endId, PAIEMENT_END_2_END_ID);
        return proxy.respondToCancellation(end2endId, new ConfirmationRequest(decision));
    }
}
