package io.github.razacki.resource.wrapper;

import io.github.razacki.representation.CompteRepresentation;
import io.github.razacki.representation.PagedResponse;
import io.github.razacki.resource.ComptesResource;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;

public class ComptesResourceWrapper extends ListableWrapper<CompteRepresentation, ComptesResource> {

    private TransactionResourceWrapper transactions;

    public ComptesResourceWrapper(ComptesResource proxy, WebTarget target) {
        super(proxy, target, new GenericType<PagedResponse<CompteRepresentation>>() {});
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
