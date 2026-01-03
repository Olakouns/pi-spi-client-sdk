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

import io.github.olakouns.representation.ConfirmationRequest;
import io.github.olakouns.representation.DemandesPaiementsRepresentation;
import io.github.olakouns.representation.PagedResponse;

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
