package io.github.razacki;

import io.github.razacki.representation.CompteRepresentation;
import io.github.razacki.representation.DemandesPaiementsRepresentation;
import io.github.razacki.representation.PagedResponse;
import io.github.razacki.representation.enums.CompteType;
import io.github.razacki.representation.enums.PaiementCategory;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

public class DocCodeTest {

    public static void main(String[] args) {

        PiSpiClient piSpi = PiSpiClientBuilder.builder()
                .serverUrl("https://votre-base-url.com/v1")
                .clientId("votre-client-id")
                .clientSecret("votre-secret-confidentiel")
                .build();

        DemandesPaiementsRepresentation rtp = DemandesPaiementsRepresentation.builder()
                .categorie(PaiementCategory.CODE_401)
                .txId("23511722")
                .payeurAlias("8b1b2499-3e50-435b-b757-ac7a83d8aa7f")
                .payeAlias("8b1b2499-3e50-435b-b757-ac7a83d8aa8c")
                .confirmation(true)
                .montant(BigDecimal.valueOf(3000000))
                .dateLimitePaiement(OffsetDateTime.now().plusDays(8))
                .build();


        // 2. Envoi de la demande via le module de paiements
        DemandesPaiementsRepresentation response = piSpi
                .api()
                .demandesPaiements()
                .create(rtp);

        System.out.println("Demande envoyée ! Statut : " + response.getStatut());


        // 1. Envoi de la requête paginée pour lister les comptes
        PagedResponse<CompteRepresentation> pagedResponse = piSpi.api()
                .comptes()
                .list("1", 20);

        System.out.println("Total des comptes : " + pagedResponse.getMeta().getTotal());

        // 2. Envoi de la requête filtrée:
        // URL générée ${BASE_URL}/comptes?numero[contains]=CIC23442&type[eq]=CACC&sort=statut&page=0&size=20
        PagedResponse<CompteRepresentation> filterPagedResponse = piSpi.api()
                .comptes()
                .query()
                .filter(filterBuilder -> {
                    filterBuilder
                            .contains("numero", "CIC23442") // numero[contains]=CIC23442
                            .eq("type", CompteType.CACC) // type[eq]=CACC
                            .sortAsc("statut"); // sort=statut
                })
                .page("0")
                .size(20)
                .execute();

        System.out.println("Total des comptes : " + filterPagedResponse.getMeta().getTotal());


        piSpi.api().comptes()
                .query()
                .filter(f -> f.sortAsc("statut"))
                .execute();


        Map<String, String> filtresDynamiques = new HashMap<>();
        filtresDynamiques.put("numero[contains]", "CIC2344");
        filtresDynamiques.put("type[eq]", "CACC");

        PagedResponse<CompteRepresentation> resultats = piSpi.api()
                .comptes()
                .query()
                .filters(filtresDynamiques) // Passage direct de la Map
                .execute();


        piSpi.api().comptes()
                .query()
                .filter(f -> f.sortAsc("statut"))
                .execute();

        piSpi.api().comptes()
                .query()
                .filter(f -> f.sortDesc("statut"))
                .execute();

        piSpi.api().comptes()
                .query()
                .filter(f -> f.sort("statut", true))
                .execute();

        piSpi.api().comptes()
                .query()
                .filter(f -> f.sort("statut", false))
                .execute();

        piSpi.api().comptes()
                .query()
                .filter(f -> f.sort("statut", false, "solde", true))
                .execute();

    }


}

