package io.github.razacki.resource;

import io.github.razacki.representation.CompteRepresentation;
import io.github.razacki.representation.PagedResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

public interface ComptesResource extends PageableResource<CompteRepresentation>{
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    PagedResponse<CompteRepresentation> list(@QueryParam("page") String page, @QueryParam("size") int size);

    @GET
    @Path("/{numero}")
    @Produces(MediaType.APPLICATION_JSON)
    CompteRepresentation findByNumero(@PathParam("numero") String numero);

    @Path("/transactions")
    TransactionsResource transactions();

    @Path("/{numero}/alias")
    AliasResource alias(@PathParam("numero") String numero);
}
