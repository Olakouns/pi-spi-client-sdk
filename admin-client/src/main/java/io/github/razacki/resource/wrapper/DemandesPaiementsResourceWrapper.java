package io.github.razacki.resource.wrapper;

import io.github.razacki.representation.ConfirmationRequest;
import io.github.razacki.representation.DemandesPaiementsRepresentation;
import io.github.razacki.representation.PagedResponse;
import io.github.razacki.resource.DemandesPaiementsResource;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;

public class DemandesPaiementsResourceWrapper extends ListableWrapper<DemandesPaiementsRepresentation, DemandesPaiementsResource> {
    private static final String DEMANDES_PAIEMENTS_ID = "DemandesPaiements ID" ;
    public DemandesPaiementsResourceWrapper(DemandesPaiementsResource proxy, WebTarget target) {
        super(proxy, target, new GenericType<PagedResponse<DemandesPaiementsRepresentation>>() {});
    }

    public DemandesPaiementsRepresentation create(DemandesPaiementsRepresentation demandesPaiementsRepresentation) {
        validateNotNull(demandesPaiementsRepresentation, "demandesPaiementsRepresentation");
        return proxy.create(demandesPaiementsRepresentation);
    }

    public DemandesPaiementsRepresentation findById(String id) {
        validateNotEmpty(id, DEMANDES_PAIEMENTS_ID);
        return proxy.findById(id);
    }

    public DemandesPaiementsRepresentation confirm(String id, boolean decision) {
        validateNotEmpty(id, DEMANDES_PAIEMENTS_ID);
        return proxy.confirm(id, new ConfirmationRequest(decision));
    }

    public DemandesPaiementsRepresentation sendDecision(String id, boolean decision) {
        validateNotEmpty(id, DEMANDES_PAIEMENTS_ID);
        return proxy.sendDecision(id, new ConfirmationRequest(decision));
    }
}
