package io.github.olakouns.resource.wrapper;

import io.github.olakouns.representation.PagedResponse;
import io.github.olakouns.representation.TransactionRepresentation;
import io.github.olakouns.resource.TransactionsResource;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;

public class TransactionResourceWrapper extends ListableWrapper<TransactionRepresentation, TransactionsResource> {
    public TransactionResourceWrapper(TransactionsResource proxy, WebTarget target) {
        super(proxy, target, new GenericType<PagedResponse<TransactionRepresentation>>() {});
    }

    public TransactionRepresentation create(TransactionRepresentation transactionsRepresentation){
        validateNotNull(transactionsRepresentation, "transactionsRepresentation");
        return proxy.create(transactionsRepresentation);
    }
}
