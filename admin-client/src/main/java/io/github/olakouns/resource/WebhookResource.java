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

import io.github.olakouns.representation.PagedResponse;
import io.github.olakouns.representation.WebhookRenewRequest;
import io.github.olakouns.representation.WebhookRepresentation;
import io.github.olakouns.representation.WebhookRequest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

public interface WebhookResource extends PageableResource<WebhookRepresentation> {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    PagedResponse<WebhookRepresentation> list(@QueryParam("page") String page, @QueryParam("size") int size);

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    WebhookRepresentation create(WebhookRequest webhookRepresentation);

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    WebhookRepresentation findById(@PathParam("id") String id);

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    WebhookRepresentation update(@PathParam("id") String id, WebhookRequest webhookRepresentation);

    @DELETE
    @Path("/{id}")
    void delete(@PathParam("id") String id);

    @POST
    @Path("/{id}/secrets")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    WebhookRepresentation renewSecret(@PathParam("id") String id, WebhookRenewRequest renewRequest);
}
