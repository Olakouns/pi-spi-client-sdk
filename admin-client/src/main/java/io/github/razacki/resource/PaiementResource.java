package io.github.razacki.resource;

import io.github.razacki.representation.CancelPaymentRequest;
import io.github.razacki.representation.ConfirmationRequest;
import io.github.razacki.representation.PagedResponse;
import io.github.razacki.representation.PaiementRepresentation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

public interface PaiementResource extends PageableResource<PaiementRepresentation> {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    PagedResponse<PaiementRepresentation> list(@QueryParam("page") String page, @QueryParam("size") int size);

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    PaiementRepresentation create(PaiementRepresentation paiementRepresentation);

    @GET
    @Path("/{txId}")
    @Produces(MediaType.APPLICATION_JSON)
    PaiementRepresentation findById(@PathParam("txId") String txId);

    @PUT
    @Path("/{txId}/confirmations")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    PaiementRepresentation confirm(@PathParam("txId") String txId, ConfirmationRequest confirmation);

    @GET
    @Path("/{end2endId}/statuts")
    @Produces(MediaType.APPLICATION_JSON)
    PaiementRepresentation getStatus(@PathParam("end2endId") String end2endId);

    @PUT
    @Path("/{end2endId}/retours")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    PaiementRepresentation returnFunds(@PathParam("end2endId") String end2endId);

    @POST
    @Path("/{end2endId}/annulations")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    PaiementRepresentation requestCancellation(@PathParam("end2endId") String end2endId, CancelPaymentRequest cancelPaymentRequest);

    @PUT
    @Path("/{end2endId}/annulations/reponses")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    PaiementRepresentation respondToCancellation(@PathParam("end2endId") String end2endId, ConfirmationRequest confirmation);
}