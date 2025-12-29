package io.github.razacki.resource;

import io.github.razacki.representation.EnrollmentRepresentation;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

public interface EnrollmentResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    EnrollmentRepresentation check();
}
