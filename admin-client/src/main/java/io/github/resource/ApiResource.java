package io.github.resource;


import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

@Consumes(MediaType.APPLICATION_JSON)
public interface ApiResource {
    @Path("/comptes")
    ComptesResource comptes();

    @Path("/alias/{cle}")
    EnrollmentResource enrollment(@PathParam("cle") String cle);

    @Path("/webhooks")
    WebhookResource webhooks();

    @Path("/demandes-paiements")
    DemandesPaiementsResource demandesPaiements();

    @Path("/demandes-paiements-groupes")
    DemandePaiementGroupeResource demandesPaiementsGroupes();

    @Path("/paiements")
    PaiementResource paiements();
}
