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


import io.github.olakouns.representation.AliasRepresentation;
import io.github.olakouns.representation.CreateAliasRequest;
import io.github.olakouns.representation.PagedResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

public interface AliasResource extends PageableResource<AliasRepresentation> {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    PagedResponse<AliasRepresentation> list(@QueryParam("page") String page, @QueryParam("size") int size);


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    AliasRepresentation create(CreateAliasRequest createAliasRequest);

    @DELETE
    @Path("/{cle}")
    void delete(@PathParam("cle") String cle);
}
