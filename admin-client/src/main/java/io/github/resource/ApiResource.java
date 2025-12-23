package io.github.resource;


import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

@Consumes(MediaType.APPLICATION_JSON)
public interface ApiResource {

    @Path("/comptes")
    ComptesResource comptes();

}
