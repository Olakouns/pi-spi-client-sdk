package io.github.olakouns.resource;


import io.github.olakouns.representation.AliasRepresentation;
import io.github.olakouns.representation.CreateAliasRequest;
import io.github.olakouns.representation.PagedResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

public interface AliasResource extends PageableResource<AliasRepresentation> {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    PagedResponse<AliasRepresentation> list(@QueryParam("page") String page, @QueryParam("size") int size);


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    AliasRepresentation create(CreateAliasRequest createAliasRequest);

    @DELETE
    @Path("/{cle}")
    void delete(@PathParam("cle") String cle);
}
