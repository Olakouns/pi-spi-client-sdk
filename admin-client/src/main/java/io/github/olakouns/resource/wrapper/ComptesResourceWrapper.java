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

import io.github.olakouns.representation.CompteRepresentation;
import io.github.olakouns.representation.PagedResponse;
import io.github.olakouns.resource.ComptesResource;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;

public class ComptesResourceWrapper extends ListableWrapper<CompteRepresentation, ComptesResource> {

    private TransactionResourceWrapper transactions;

    public ComptesResourceWrapper(ComptesResource proxy, WebTarget target) {
        super(proxy, target, new GenericType<PagedResponse<CompteRepresentation>>() {});
    }

    public CompteRepresentation findByNumero(String numero) {
        validateNotEmpty(numero, "Compte numero");
        return proxy.findByNumero(numero);
    }

    public synchronized TransactionResourceWrapper transactions() {
        if (this.transactions == null) {
            this.transactions = new TransactionResourceWrapper(proxy.transactions(), target.path("/transactions"));
        }
        return this.transactions;
    }

    public AliasResourceWrapper alias(String numero) {
        validateNotEmpty(numero, "Compte numero");
        return new AliasResourceWrapper(
                proxy.alias(numero),
                target.path("/{numero}/alias").resolveTemplate("numero", numero)
        );
    }
}
