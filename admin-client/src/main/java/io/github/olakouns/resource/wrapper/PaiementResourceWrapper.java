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

import io.github.olakouns.representation.CancelPaymentRequest;
import io.github.olakouns.representation.ConfirmationRequest;
import io.github.olakouns.representation.PagedResponse;
import io.github.olakouns.representation.PaiementRepresentation;
import io.github.olakouns.representation.enums.PaiementAnnulationMotif;
import io.github.olakouns.resource.PaiementResource;

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
