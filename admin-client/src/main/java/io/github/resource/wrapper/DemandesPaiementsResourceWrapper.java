package io.github.resource.wrapper;

import io.github.representation.ConfirmationRequest;
import io.github.representation.DemandesPaiementsRepresentation;
import io.github.resource.DemandesPaiementsResource;

import javax.ws.rs.client.WebTarget;

public class DemandesPaiementsResourceWrapper extends BaseWrapper<DemandesPaiementsRepresentation, DemandesPaiementsResource> {
    private static final String DEMANDESPAIEMENTS_ID = "DemandesPaiements ID" ;
    public DemandesPaiementsResourceWrapper(DemandesPaiementsResource proxy, WebTarget target) {
        super(proxy, target);
    }

    public DemandesPaiementsRepresentation create(DemandesPaiementsRepresentation demandesPaiementsRepresentation) {
        validateNotNull(demandesPaiementsRepresentation, "demandesPaiementsRepresentation");
        return proxy.create(demandesPaiementsRepresentation);
    }

    public DemandesPaiementsRepresentation findById(String id) {
        validateNotEmpty(id, DEMANDESPAIEMENTS_ID);
        return proxy.findById(id);
    }

    public DemandesPaiementsRepresentation confirm(String id, boolean decision) {
        validateNotEmpty(id, DEMANDESPAIEMENTS_ID);
        return proxy.confirm(id, new ConfirmationRequest(decision));
    }

    public DemandesPaiementsRepresentation sendDecision(String id, boolean decision) {
        validateNotEmpty(id, DEMANDESPAIEMENTS_ID);
        return proxy.sendDecision(id, new ConfirmationRequest(decision));
    }
}
