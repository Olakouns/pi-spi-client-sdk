package io.github.resource.wrapper;

import io.github.representation.PagedResponse;
import io.github.representation.TransactionRepresentation;
import io.github.resource.TransactionsResource;

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
