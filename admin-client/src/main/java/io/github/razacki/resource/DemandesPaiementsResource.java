package io.github.razacki.resource;

import io.github.razacki.representation.ConfirmationRequest;
import io.github.razacki.representation.DemandesPaiementsRepresentation;
import io.github.razacki.representation.PagedResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

public interface DemandesPaiementsResource extends PageableResource<DemandesPaiementsRepresentation> {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    PagedResponse<DemandesPaiementsRepresentation> list(@QueryParam("page") String page, @QueryParam("size") int size);

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    DemandesPaiementsRepresentation create(DemandesPaiementsRepresentation demandesPaiementsRepresentation);

    @GET
    @Path("/{txId}")
    @Produces(MediaType.APPLICATION_JSON)
    DemandesPaiementsRepresentation findById(@PathParam("txId") String txId);

    @PUT
    @Path("/{txId}/confirmations")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    DemandesPaiementsRepresentation confirm(@PathParam("txId") String txId, ConfirmationRequest confirmation);

    @PUT
    @Path("/{txId}/reponses")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    DemandesPaiementsRepresentation sendDecision(@PathParam("txId") String txId, ConfirmationRequest confirmation);
}
