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
