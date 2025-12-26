package io.github.resource.wrapper;

import io.github.representation.CompteRepresentation;
import io.github.resource.ComptesResource;

import javax.ws.rs.client.WebTarget;

public class ComptesResourceWrapper extends ListableWrapper<CompteRepresentation, ComptesResource> {

    private TransactionResourceWrapper transactions;

    public ComptesResourceWrapper(ComptesResource proxy, WebTarget target) {
        super(proxy, target);
    }

    public CompteRepresentation findByNumero(String numero) {
        return proxy.findByNumero(numero);
    }

    public synchronized TransactionResourceWrapper transactions() {
        if (this.transactions == null) {
            this.transactions = new TransactionResourceWrapper(proxy.transactions(), target.path("/transactions"));
        }
        return this.transactions;
    }

    public AliasResourceWrapper alias(String numero) {
        return new AliasResourceWrapper(
                proxy.alias(numero),
                target.path("/{numero}/alias").resolveTemplate("numero", numero)
        );
    }
}
