package io.github.razacki.resource;

import io.github.razacki.representation.ConfirmationRequest;
import io.github.razacki.representation.DemandePaiementGroupeRequest;
import io.github.razacki.representation.PaiementGroupeRepresentation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

public interface DemandePaiementGroupeResource extends PageableResource<PaiementGroupeRepresentation> {
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    void create(DemandePaiementGroupeRequest demandePaiementGroupeRequest);

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
