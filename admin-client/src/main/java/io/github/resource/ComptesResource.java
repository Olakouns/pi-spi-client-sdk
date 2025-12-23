package io.github.resource;

import io.github.representation.CompteRepresentation;
import io.github.representation.PagedResponse;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

public interface ComptesResource {
    @GET
    @Path("/comptes")
    @Produces(MediaType.APPLICATION_JSON)
    PagedResponse<CompteRepresentation> list(@QueryParam("page") int page, @QueryParam("size") int size);
}
