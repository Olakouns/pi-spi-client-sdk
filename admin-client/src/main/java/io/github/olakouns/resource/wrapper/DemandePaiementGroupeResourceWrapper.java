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
import io.github.olakouns.representation.DemandePaiementGroupeRequest;
import io.github.olakouns.representation.PaiementGroupeRepresentation;
import io.github.olakouns.resource.DemandePaiementGroupeResource;

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


    public PaiementGroupeRepresentation findById(String instructionId) {
        validateNotEmpty(instructionId, DEMANDE_PAIEMENT_GROUPE_ID);
        return proxy.findById(instructionId);
    }


    public PaiementGroupeRepresentation confirm(String instructionId, boolean decision) {
        validateNotEmpty(instructionId, DEMANDE_PAIEMENT_GROUPE_ID);
        return proxy.confirm(instructionId, new ConfirmationRequest(decision));
    }
}
