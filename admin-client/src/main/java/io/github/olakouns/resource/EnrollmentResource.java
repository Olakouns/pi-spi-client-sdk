package io.github.olakouns.resource;

import io.github.olakouns.representation.EnrollmentRepresentation;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

public interface EnrollmentResource {
    @GET
    @Path("/{cle}")
    @Produces(MediaType.APPLICATION_JSON)
    EnrollmentRepresentation check(@PathParam("cle") String cle);
}
