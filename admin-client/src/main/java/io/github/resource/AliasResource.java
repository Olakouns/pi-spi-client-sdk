package io.github.resource;


import io.github.representation.AliasRepresentation;
import io.github.representation.CreateAliasRequest;
import io.github.representation.PagedResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

public interface AliasResource extends PageableResource<AliasRepresentation> {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    PagedResponse<AliasRepresentation> list(@QueryParam("page") int page, @QueryParam("size") int size);


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    AliasRepresentation create(CreateAliasRequest createAliasRequest);

    @DELETE
    @Path("/{cle}")
    void delete(@PathParam("cle") String cle);
}
