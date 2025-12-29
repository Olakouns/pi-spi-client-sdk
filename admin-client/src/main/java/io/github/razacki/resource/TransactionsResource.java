package io.github.razacki.resource;

import io.github.razacki.representation.PagedResponse;
import io.github.razacki.representation.TransactionRepresentation;

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
