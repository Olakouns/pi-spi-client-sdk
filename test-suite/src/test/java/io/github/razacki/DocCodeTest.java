package io.github.razacki;

import io.github.razacki.representation.*;
import io.github.razacki.representation.enums.*;
import io.github.razacki.resource.wrapper.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.*;

public class DocCodeTest {

    public static void main(String[] args) {

        PiSpiClient piSpi = PiSpiClientBuilder.builder()
                .serverUrl("https://votre-base-url.com/v1")
                .clientId("votre-client-id")
                .clientSecret("votre-secret-confidentiel")
                .build();

//        DemandesPaiementsRepresentation rtp = DemandesPaiementsRepresentation.builder()
//                .categorie(PaiementCategory.CODE_401)
//                .txId("23511722")
//                .payeurAlias("8b1b2499-3e50-435b-b757-ac7a83d8aa7f")
//                .payeAlias("8b1b2499-3e50-435b-b757-ac7a83d8aa8c")
//                .confirmation(true)
//                .montant(BigDecimal.valueOf(3000000))
//                .dateLimitePaiement(OffsetDateTime.now().plusDays(8))
//                .build();
//
//
//        // 2. Envoi de la demande via le module de paiements
//        DemandesPaiementsRepresentation response = piSpi
//                .api()
//                .demandesPaiements()
//                .create(rtp);
//
//        System.out.println("Demande envoyée ! Statut : " + response.getStatut());
//
//
//        // 1. Envoi de la requête paginée pour lister les comptes
//        PagedResponse<CompteRepresentation> pagedResponse = piSpi.api()
//                .comptes()
//                .list("1", 20);
//
//        System.out.println("Total des comptes : " + pagedResponse.getMeta().getTotal());
//
//        // 2. Envoi de la requête filtrée:
//        // URL générée ${BASE_URL}/comptes?numero[contains]=CIC23442&type[eq]=CACC&sort=statut&page=0&size=20
//        PagedResponse<CompteRepresentation> filterPagedResponse = piSpi.api()
//                .comptes()
//                .query()
//                .filter(filterBuilder -> {
//                    filterBuilder
//                            .contains("numero", "CIC23442") // numero[contains]=CIC23442
//                            .eq("type", CompteType.CACC) // type[eq]=CACC
//                            .sortAsc("statut"); // sort=statut
//                })
//                .page("0")
//                .size(20)
//                .execute();
//
//        System.out.println("Total des comptes : " + filterPagedResponse.getMeta().getTotal());
//
//
//        piSpi.api().comptes()
//                .query()
//                .filter(f -> f.sortAsc("statut"))
//                .execute();
//
//
//        Map<String, String> filtresDynamiques = new HashMap<>();
//        filtresDynamiques.put("numero[contains]", "CIC2344");
//        filtresDynamiques.put("type[eq]", "CACC");
//
//        PagedResponse<CompteRepresentation> resultats = piSpi.api()
//                .comptes()
//                .query()
//                .filters(filtresDynamiques) // Passage direct de la Map
//                .execute();
//
//
//        piSpi.api().comptes()
//                .query()
//                .filter(f -> f.sortAsc("statut"))
//                .execute();
//
//        piSpi.api().comptes()
//                .query()
//                .filter(f -> f.sortDesc("statut"))
//                .execute();
//
//        piSpi.api().comptes()
//                .query()
//                .filter(f -> f.sort("statut", true))
//                .execute();
//
//        piSpi.api().comptes()
//                .query()
//                .filter(f -> f.sort("statut", false))
//                .execute();
//
//        piSpi.api().comptes()
//                .query()
//                .filter(f -> f.sort("statut", false, "solde", true))
//                .execute();
//
//
//        // COmpates
//
//        //import io.github.razacki.resource.wrapper.ComptesResourceWrapper;
//
//        ComptesResourceWrapper accounts = piSpi.api().comptes();
//
//        PagedResponse<CompteRepresentation> accountPage = accounts.list("1", 10);
//
//        PagedResponse<CompteRepresentation> response = accounts.query()
//                .filter(f -> f
//                        .eq("type", "CACC")
//                        .contains("numero", "CIC234425672778")
//                )
//                .page("0")
//                .size(10)
//                .execute();
//
//
//        // Récupération par numero
//        CompteRepresentation monCompte = accounts.findByNumero("ACC-778822");
//
//        PagedResponse<TransactionRepresentation> response = accounts.transactions().list("0", 10);
//
//        PagedResponse<TransactionRepresentation> responseFilter = accounts.transactions().query()
//                .filter(f -> f
//                        .eq("statut", TransactionStatut.INITIE)
//                        .lt("montant", 2000)
//                        .contains("txId", "1722")
//                )
//                .page("0")
//                .size(10)
//                .execute();
//
//        TransactionRepresentation createTransfertResponse = accounts.transactions().create(
//                TransactionRepresentation.builder()
//                        .txId("1722")
//                        .payeurNumero("ac7a83d8aa7f")
//                        .payeNumero("jdko7588578")
//                        .montant(BigDecimal.valueOf(150000))
//                        .build()
//        );
//
//        AliasResourceWrapper aliasApi = piSpi.api()
//                .comptes()
//                .alias("CIC2344256727788288822"); // {numero} du compte
//
//        PagedResponse<AliasRepresentation> responseV1 = aliasApi.list("0", 5);
//
//        PagedResponse<AliasRepresentation> responseV2 = aliasApi.query()
//                .page("0")
//                .size(5)
//                .execute();
//
//        AliasRepresentation createAliasResponse = aliasApi.create(AliasType.MCOD);
//
//        AliasRepresentation createAliasResponse2 = aliasApi.create(AliasType.SHID);
//
//        aliasApi.delete("cle"); // {cle}
//
//        WebhookResourceWrapper webhookApi = piSpi.api().webhooks();
//
//        PagedResponse<WebhookRepresentation> responseWebhook = webhookApi.list("0", 5);
//        PagedResponse<WebhookRepresentation> responseWebhook2 = webhookApi.query()
//                .page("0")
//                .size(5)
//                .execute();
//
//// Callback URL par événement et alias
//        WebhookRepresentation createWebhookResponse = webhookApi.create(
//                WebhookRequest.builder()
//                        .callbackUrl("https://businessmerchant.com/api/piz/webhooks/")
//                        .alias("9b1b2499-3e50-435b-b757-ac7a83d8aa8c")
//                        .events(Collections.singletonList(
//                                PiWebhookEvent.PAIEMENT_RECU
//                        ))
//                        .build()
//        );
//
//// Callback URL général
//        WebhookRepresentation createWebhookResponse = webhookApi.create(
//                WebhookRequest.builder()
//                        .callbackUrl("https://businessmerchant.com/api/piz/webhooks/")
//                        .build()
//        );
//
//// Callback URL par événement
//        WebhookRepresentation createWebhookResponse = webhookApi.create(
//                WebhookRequest.builder()
//                        .callbackUrl("https://businessmerchant.com/api/piz/webhooks/")
//                        .events(new ArrayList<>(
//                                Arrays.asList(
//                                        PiWebhookEvent.RTP_RECU,
//                                        PiWebhookEvent.RTP_REJETE
//                                        //..
//                                )
//                        ))
//                        .build()
//        );
//
////  Callback URL par alias
//        WebhookRepresentation createWebhookResponse = webhookApi.create(
//                WebhookRequest.builder()
//                        .callbackUrl("https://businessmerchant.com/api/piz/webhooks/")
//                        .alias("9b1b2499-3e50-435b-b757-ac7a83d8aa8c")
//                        .build()
//        );
//
//        WebhookRepresentation webhookDetails = webhookApi.findById("id");
//
//
////  Modifier uniquement l'URL de callback
//        WebhookRepresentation updateWebhook = webhookApi.update(
//                "12345-7785-zrr5r5v-rf5v5f-r5ffc55a", // {id} du webhook à mettre à jour
//                WebhookRequest.builder()
//                        .callbackUrl("https://businessmerchant.com/api/piz/webhooks/")
//                        .build());
//
////  Modifier l'alias associé au webhook
//        WebhookRepresentation updateWebhook = webhookApi.update(
//                "12345-7785-zrr5r5v-rf5v5f-r5ffc55a", // {id} du webhook à mettre à jour
//                WebhookRequest.builder()
//                        .alias("9b1b2499-3e50-435b-b757-ac7a83d8aa8c")
//                        .build());
//
////  Modifier la liste des événements
//        WebhookRepresentation updateWebhook = webhookApi.update(
//                "12345-7785-zrr5r5v-rf5v5f-r5ffc55a", // {id} du webhook à mettre à jour
//                WebhookRequest.builder()
//                        .events(new ArrayList<>(
//                                Arrays.asList(
//                                        PiWebhookEvent.PAIEMENT_RECU,
//                                        PiWebhookEvent.PAIEMENT_ENVOYE,
//                                        PiWebhookEvent.PAIEMENT_REJETE,
//                                        PiWebhookEvent.RTP_RECU,
//                                        PiWebhookEvent.RTP_REJETE
//                                        // ..
//                                )
//                        ))
//                        .build());
//
//        webhookApi.delete("12345-7785-zrr5r5v-rf5v5f-r5ffc55a"); // {id} du webhook à supprimer
//
//        WebhookRepresentation renewedSecret = webhookApi.renewSecret(
//                "12345-7785-zrr5r5v-rf5v5f-r5ffc55a", // {id} du webhook
//                OffsetDateTime.now().plusMonths(6) // Nouvelle date d'expiration du secret
//        );
//
//        DemandesPaiementsResourceWrapper demandesPaiementsApi = piSpi.api().demandesPaiements();
//
//        PagedResponse<DemandesPaiementsRepresentation> response = demandesPaiementsApi.list("0", 10);
//
//        PagedResponse<DemandesPaiementsRepresentation> response1 = demandesPaiementsApi.query()
//                .filter(f ->
//                        f
//                                .contains("payeAlias", "9b1b2499-3e50-435b-b757-ac")
//                                .contains("txId", "RTP-2023")
//                )
//                .page("0")
//                .size(10)
//                .execute();
//
//
//        // Confirmation requise
//        DemandesPaiementsRepresentation createDemandePaiement = demandesPaiementsApi.create(
//                DemandesPaiementsRepresentation.builder()
//                        .categorie(PaiementCategory.CODE_401)
//                        .txId("23511722")
//                        .payeurAlias("8b1b2499-3e50-435b-b757-ac7a83d8aa7f")
//                        .payeAlias("8b1b2499-3e50-435b-b757-ac7a83d8aa8c")
//                        .montant(BigDecimal.valueOf(3000000))
//                        .confirmation(true)
//                        .dateLimitePaiement(OffsetDateTime.now().plusMonths(6))
//                        .build()
//        );
//
//        // Paiement immédiat sur site (point de vente physique)
//        DemandesPaiementsRepresentation createDemandePaiement = demandesPaiementsApi.create(
//                DemandesPaiementsRepresentation.builder()
//                        .categorie(PaiementCategory.CODE_500)
//                        .txId("RTP-2023-001")
//                        .confirmation(false)
//                        .payeurAlias("9b1b3499-3e50-435b-b757-ac7a83d8aa96")
//                        .payeAlias("9b1b2499-3e50-435b-b757-ac7a83d8aa8c")
//                        .montant(BigDecimal.valueOf(15000))
//                        .motif("Achat en magasin")
//                        .build()
//        );
//
//        // Confirmation non requise
//        DemandesPaiementsRepresentation createDemandePaiement = demandesPaiementsApi.create(
//                DemandesPaiementsRepresentation.builder()
//                        .txId("23552722")
//                        .payeurAlias("8b1b2499-3e50-435b-b757-ac7a83d8aa7f")
//                        .payeAlias("8b1b2499-3e50-435b-b757-ac7a83d8aa8c")
//                        .montant(BigDecimal.valueOf(3000000))
//                        .confirmation(false)
//                        .dateLimitePaiement(OffsetDateTime.now().plusDays(8))
//                        .build()
//        );
//
//        // etc.
//
//        DemandesPaiementsRepresentation demandeDetails = demandesPaiementsApi.findById("23552722"); // {txId} de la demande
//
//
//        DemandesPaiementsRepresentation response = demandesPaiementsApi.confirm(
//                "23511722", // {txId} de la demande
//                true // décision de confirmation
//        );
//
//        DemandesPaiementsRepresentation response = demandesPaiementsApi.sendDecision(
//                "23511722", // {txId} de la demande
//                ConfirmationRequest.builder()
//                        .decision(false)  // accepter ou rejeter la demande de paiement
//                        .raison(StatutRaison.AM09) // motif du rejet si applicable
//                        .build()
//        );


//Paiement en ligne avec débit différé (BNPL e-commerce)
//        DemandesPaiementsRepresentation createDemandePaiement = demandesPaiementsApi.create(
//                DemandesPaiementsRepresentation.builder()
//                        .categorie(PaiementCategory.CODE_521)
//                        .txId("RTP-2023-007")
//                        .confirmation(false)
//                        .payeurAlias("9b1b3499-3e50-435b-b757-ac7a83d8aa96")
//                        .payeAlias("9b1b2499-3e50-435b-b757-ac7a83d8aa8c")
//                        .montant(BigDecimal.valueOf(100000))
//                        .debitDiffere(true)
//                        .remise(RemiseRepresentation.builder()
//                                .montant(BigDecimal.valueOf(1000))
//                                .build()
//                        )
//                        .refDocType(RefDocType.PUOR)
//                        .refDocNumero("WEB-2023-789")
//                        .build()
//        );


//        DemandePaiementGroupeResourceWrapper batchApi = piSpi.api().demandesPaiementsGroupes();
//
//        DemandePaiementGroupeRequest batchRequest = DemandePaiementGroupeRequest.builder()
//                .instructionId("FACTURES-2025-01-1")
//                .payeAlias("8b1b2499-3e50-435b-b757-ac7a83d8aa7f")
//                .confirmation(false)
//                .transactions(
//                        Arrays.asList(
//                                DemandesPaiementsRepresentation.builder()
//                                        .txId("123-456-789")
//                                        .payeurAlias("9b1b2499-3e50-435b-b757-ac7a83d8aa8c")
//                                        .montant(BigDecimal.valueOf(150000000))
//                                        .dateLimitePaiement(OffsetDateTime.parse("2023-12-31T23:59:59.999Z"))
//                                        .build(),
//                                DemandesPaiementsRepresentation.builder()
//                                        .txId("987-000-234")
//                                        .payeurAlias("s03b2499-3e50-435b-b757-ac7a83d8aa8c")
//                                        .refDocNumero("FAC-2023-001")
//                                        .refDocType(RefDocType.CINV)
//                                        .montant(BigDecimal.valueOf(3000000))
//                                        .dateLimitePaiement(OffsetDateTime.parse("2023-12-31T23:59:59.999Z"))
//                                        .build()
//
//                        )
//                )
//                .build();
//        batchApi.create(batchRequest);

//        PaiementGroupeRepresentation response = batchApi.findById("RTP-MASSE-2025-01");

// response.getStatut()
// response.getTransactionsTotal()

//        PaiementGroupeRepresentation response = batchApi.confirm("RTP-MASSE-2025-01", true);


//        PagedResponse<PaiementRepresentation> history = paiementApi.list("1", 20);
//
//        PagedResponse<PaiementRepresentation> history = paiementApi.query()
//                .filter(f -> f
//                                .contains("payeAlias", "b1b3499-3e50")
//                                .gt("montantAchat", 10000)
//                        // .. autres filtres possibles
//                )
//                .page("1")
//                .size(20)
//                .execute();

//// Paiement immediat - confirmation demandée
//        PaiementRepresentation response = paiementApi.create(
//                PaiementRepresentation.builder()
//                        .txId("123-456-789")
//                        .payeurAlias("8b1b2499-3e50-435b-b757-ac7a83d8aa7f")
//                        .payeAlias("9b1b2499-3e50-435b-b757-ac7a83d8aa8c")
//                        .montant(BigDecimal.valueOf(150000000))
//                        .confirmation(true)
//                        .build()
//        );
//
////Paiement immediat - confirmation non requise
//        PaiementRepresentation response = paiementApi.create(
//                PaiementRepresentation.builder()
//                        .txId("123-456-789")
//                        .payeurAlias("8b1b2499-3e50-435b-b757-ac7a83d8aa7f")
//                        .payeAlias("9b1b2499-3e50-435b-b757-ac7a83d8aa8c")
//                        .montant(BigDecimal.valueOf(150000000))
//                        .confirmation(false)
//                        .build()
//        );

//        PaiementRepresentation response = paiementApi.findById("Tag-a7d96dfdsdf965dfdsdf");
//
//        PaiementRepresentation response = paiementApi.confirm("23552722", true);
//
//        PaiementRepresentation response = paiementApi.getStatus("ESNB00120230221103000KXvozkpNvhUk9e");

        PaiementGroupeResourceWrapper bulkApi = piSpi.api().paiementsGroupes();

//        {
//            "instructionId": "SALAIRES-2025-01-1",
//                "payeurAlias": "8b1b2499-3e50-435b-b757-ac7a83d8aa7f",
//                "confirmation": true,
//                "motif": "Salaire de janvier 2025",
//                "transactions": [
//            {
//                "txId": "SAL-202501-C06946-1",
//                    "payeAlias": "9b1b2499-3e50-435b-b757-ac7a83d8aa8c",
//                    "montant": 1500000
//            },
//            {
//                "txId": "SAL-202501-C06947-2",
//                    "payeAlias": "s03b2499-3e50-635b-b757-ac7a83d8aa8c",
//                    "montant": 1000000
//            },
//
//  ]
//        }

// Paiement en masse de salaires avec confirmation
        PaiementGroupeRequest batchRequest = PaiementGroupeRequest.builder()
                .instructionId("SALAIRES-2025-01-1")
                .payeurAlias("b1b2499-3e50-435b-b757-ac7a83d8aa7f")
                .confirmation(true)
                .motif("Salaire de janvier 2025")
                .transactions(
                        Arrays.asList(
                                PaiementRepresentation.builder()
                                        .txId("SAL-202501-C06946-1")
                                        .payeAlias("9b1b2499-3e50-435b-b757-ac7a83d8aa8c")
                                        .montant(BigDecimal.valueOf(1500000))
                                        .build(),
                                PaiementRepresentation.builder()
                                        .txId("SAL-202501-C06947-2")
                                        .payeAlias("s03b2499-3e50-435b-b757-ac7a83d8aa8c")
                                        .montant(BigDecimal.valueOf(1000000))
                                        .build()

                        )
                )
                .build();

        bulkApi.create(batchRequest);


// Paiement en masse de salaires sans confirmation
        PaiementGroupeRequest batchRequest2 = PaiementGroupeRequest.builder()
                .instructionId("FOURNISSEURS-2025-01-1")
                .payeurAlias("8b1b2499-3e50-435b-b757-ac7a83d8aa7f")
                .confirmation(false)
                .transactions(
                        Arrays.asList(
                                PaiementRepresentation.builder()
                                        .txId("FOURN-202501-C06946-1")
                                        .payeAlias("9b1b2499-3e50-435b-b757-ac7a83d8aa8c")
                                        .montant(BigDecimal.valueOf(1500000))
                                        .setRefDocNumero("FAC-FOURNISSEUR-1-2025-001")
                                        .refDocType(RefDocType.CINV)
                                        .build(),
                                PaiementRepresentation.builder()
                                        .txId("FOURN-202501-C06947-2")
                                        .payeAlias("s03b2499-3e50-635b-b757-ac7a83d8aa8c")
                                        .montant(BigDecimal.valueOf(1000000))
                                        .setRefDocNumero("FAC-FOURNISSEUR-2-2025-002")
                                        .refDocType(RefDocType.CINV)
                                        .build()

                        )
                )
                .build();
        bulkApi.create(batchRequest2);

        PaiementGroupeRepresentation response = bulkApi.findById("SALAIRES-2025-01");
        //response.getTransactionsTotal();
        //response.getStatut();
        //response.getTransactionsEnvoyees();
        //response.getTransactionsInitiees();
        // etc

        PaiementGroupeRepresentation confirmResponse = bulkApi.confirm("SALAIRES-2025-01-1", true);


        // Exemple
        PaiementResourceWrapper paiementApi = piSpi.api().paiements();

        //        PaiementRepresentation response = paiementApi.returnFunds("ESNB00120230221103000KXvozkpNvhUk9e");

        //PaiementRepresentation response = paiementApi.requestCancellation("ESNB00120230221103000KXvozkpNvhUk9e" , PaiementAnnulationMotif.AC03);
        // {end2endId} et la raison d'annulation

        //        PaiementRepresentation response = paiementApi.respondToCancellation("ESNB00120230221103000KXvozkpNvhUk9e" , true);

    }


}

