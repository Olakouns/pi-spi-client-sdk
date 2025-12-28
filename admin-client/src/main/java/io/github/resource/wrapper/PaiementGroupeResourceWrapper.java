package io.github.resource.wrapper;

import io.github.representation.ConfirmationRequest;
import io.github.representation.PaiementGroupeRepresentation;
import io.github.representation.PaiementGroupeRequest;
import io.github.resource.PaiementGroupeResource;

import javax.ws.rs.client.WebTarget;

public class PaiementGroupeResourceWrapper extends BaseWrapper<PaiementGroupeResource> {
    private static final String DEMANDE_PAIEMENT_GROUPE_ID = "Instruction ID";

    public PaiementGroupeResourceWrapper(PaiementGroupeResource proxy, WebTarget target) {
        super(proxy, target);
    }

    public void create(PaiementGroupeRequest paiementGroupeRequest) {
        validateNotNull(paiementGroupeRequest, "PaiementGroupeRequest");
        proxy.create(paiementGroupeRequest);
    }

    public PaiementGroupeRepresentation findById(String instructionId) {
        validateNotEmpty(instructionId, DEMANDE_PAIEMENT_GROUPE_ID);
        return proxy.findById(instructionId);
    }

    public PaiementGroupeRepresentation confirm(String instructionId, boolean decision) {
        validateNotEmpty(instructionId, DEMANDE_PAIEMENT_GROUPE_ID);
        return proxy.confirm(instructionId, new ConfirmationRequest(decision));
    }
}
