package io.github.olakouns.resource;

import io.github.olakouns.representation.PagedResponse;
import io.github.olakouns.representation.TransactionRepresentation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

public interface TransactionsResource extends PageableResource<TransactionRepresentation> {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    PagedResponse<TransactionRepresentation> list(@QueryParam("page") String page, @QueryParam("size") int size);

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    TransactionRepresentation create(TransactionRepresentation transactionsRepresentation);
}
