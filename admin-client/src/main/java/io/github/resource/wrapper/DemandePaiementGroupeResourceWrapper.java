package io.github.resource.wrapper;

import io.github.representation.ConfirmationRequest;
import io.github.representation.DemandePaiementGroupeRepresentation;
import io.github.representation.DemandePaiementGroupeRequest;
import io.github.resource.DemandePaiementGroupeResource;

import javax.ws.rs.client.WebTarget;

public class DemandePaiementGroupeResourceWrapper extends BaseWrapper<DemandePaiementGroupeResource> {
    private static final String DEMANDE_PAIEMENT_GROUPE_ID = "Instruction ID";

    public DemandePaiementGroupeResourceWrapper(DemandePaiementGroupeResource proxy, WebTarget target) {
        super(proxy, target);
    }

    public void create(DemandePaiementGroupeRequest demandePaiementGroupeRequest) {
        validateNotNull(demandePaiementGroupeRequest, "DemandePaiementGroupeRequest");
        proxy.create(demandePaiementGroupeRequest);
    }


    public DemandePaiementGroupeRepresentation findById(String instructionId) {
        validateNotEmpty(instructionId, DEMANDE_PAIEMENT_GROUPE_ID);
        return proxy.findById(instructionId);
    }


    public DemandePaiementGroupeRepresentation confirm(String instructionId, boolean decision) {
        validateNotEmpty(instructionId, DEMANDE_PAIEMENT_GROUPE_ID);
        return proxy.confirm(instructionId, new ConfirmationRequest(decision));
    }
}
