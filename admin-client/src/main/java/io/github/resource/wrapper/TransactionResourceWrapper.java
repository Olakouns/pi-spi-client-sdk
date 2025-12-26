package io.github.resource.wrapper;

import io.github.representation.TransactionRepresentation;
import io.github.resource.TransactionsResource;

import javax.ws.rs.client.WebTarget;

public class TransactionResourceWrapper extends ListableWrapper<TransactionRepresentation, TransactionsResource> {
    public TransactionResourceWrapper(TransactionsResource proxy, WebTarget target) {
        super(proxy, target);
    }

    public TransactionRepresentation create(TransactionRepresentation transactionsRepresentation){
        validateNotNull(transactionsRepresentation, "transactionsRepresentation");
        return proxy.create(transactionsRepresentation);
    }
}
