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
import io.github.olakouns.representation.PaiementGroupeRepresentation;
import io.github.olakouns.representation.PaiementGroupeRequest;
import io.github.olakouns.resource.PaiementGroupeResource;

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
