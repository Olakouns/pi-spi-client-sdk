/*
 * Copyright 2025 Razacki KOUNASSO
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.olakouns.resource.wrapper;

import io.github.olakouns.representation.ConfirmationRequest;
import io.github.olakouns.representation.DemandesPaiementsRepresentation;
import io.github.olakouns.representation.PagedResponse;
import io.github.olakouns.resource.DemandesPaiementsResource;

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

    public DemandesPaiementsRepresentation sendDecision(String id, ConfirmationRequest confirmationRequest) {
        validateNotEmpty(id, DEMANDES_PAIEMENTS_ID);
        validateNotNull(confirmationRequest, "confirmationRequest");
        return proxy.sendDecision(id, confirmationRequest);
    }
}
