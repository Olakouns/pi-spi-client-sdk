package io.github.resource;

import io.github.representation.ConfirmationRequest;
import io.github.representation.DemandePaiementGroupeRepresentation;
import io.github.representation.DemandePaiementGroupeRequest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

public interface DemandePaiementGroupeResource extends PageableResource<DemandePaiementGroupeRepresentation> {
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    void create(DemandePaiementGroupeRequest demandePaiementGroupeRequest);

    @GET
    @Path("/{instructionId}")
    @Produces(MediaType.APPLICATION_JSON)
    DemandePaiementGroupeRepresentation findById(@PathParam("instructionId") String instructionId);

    @PUT
    @Path("/{instructionId}/confirmations")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    DemandePaiementGroupeRepresentation confirm(@PathParam("instructionId") String instructionId, ConfirmationRequest confirmation);
}
