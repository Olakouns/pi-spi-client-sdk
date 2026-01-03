/*
 * Copyright 2025 Razacki KOUNASSO
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.olakouns.resource;

import io.github.olakouns.representation.CancelPaymentRequest;
import io.github.olakouns.representation.ConfirmationRequest;
import io.github.olakouns.representation.PagedResponse;
import io.github.olakouns.representation.PaiementRepresentation;

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