package io.github.resource;

import io.github.representation.ConfirmationRequest;
import io.github.representation.PaiementGroupeRepresentation;
import io.github.representation.PaiementGroupeRequest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

public interface PaiementGroupeResource extends PageableResource<PaiementGroupeRepresentation> {
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    void create(PaiementGroupeRequest paiementGroupeRequest);

    @GET
    @Path("/{instructionId}")
    @Produces(MediaType.APPLICATION_JSON)
    PaiementGroupeRepresentation findById(@PathParam("instructionId") String instructionId);

    @PUT
    @Path("/{instructionId}/confirmations")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    PaiementGroupeRepresentation confirm(@PathParam("instructionId") String instructionId, ConfirmationRequest confirmation);
}
