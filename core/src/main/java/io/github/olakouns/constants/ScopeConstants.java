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

package io.github.olakouns.constants;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Constants for OAuth2 scopes used in the PiSpi API.
 * <p>
 * These scopes define access permissions to the various resources of the API.
 * A client must request the appropriate scopes during OAuth2 authentication.
 * </p>
 */
public abstract class ScopeConstants {
    static class Compte {
        /**
         * Autorisation pour consulter les comptes
         */
        public static final String COMPTE_READ = "compte.read";

        /**
         * Autorisation d'effectuer des mouvements de fonds entre ses comptes
         */
        public static final String COMPTE_TRANSACTION_WRITE = "compte_transaction.write";

        /**
         * Autorisation pour consulter les transactions intra-comptes
         */
        public static final String COMPTE_TRANSACTION_READ = "compte_transaction.read";
    }

    static class Alias{
        /**
         * Autorisation de consulter les alias
         */
        public static final String ALIAS_READ = "alias.read";

        /**
         * Autorisation de créer les alias
         */
        public static final String ALIAS_WRITE = "alias.write";

        /**
         * Autorisation de supprimer des alias
         */
        public static final String ALIAS_DELETE = "alias.delete";
    }

    static class Webhook {
        /**
         * Autorisation de consulter le webhook
         */
        public static final String WEBHOOK_READ = "webhook.read";

        /**
         * Autorisation de créer ou modifier un webhook
         */
        public static final String WEBHOOK_WRITE = "webhook.write";

        /**
         * Autorisation de supprimer des webhooks
         */
        public static final String WEBHOOK_DELETE = "webhook.delete";
    }

    static class  DemandePaiement {
        /**
         * Autorisation de créer une demande de paiement
         */
        public static final String DEMANDE_PAIEMENT_WRITE = "demande_paiement.write";

        /**
         * Autorisation pour consulter des demandes de paiement
         */
        public static final String DEMANDE_PAIEMENT_READ = "demande_paiement.read";

        /**
         * Autorisation d'accepter ou de rejeter une demande de paiement
         */
        public static final String DEMANDE_PAIEMENT_REPONSE_WRITE = "demande_paiement_reponse.write";

        /**
         * Autorisation de créer des demandes de paiement en masse
         */
        public static final String DEMANDE_PAIEMENT_GROUPE_WRITE = "demande_paiement_groupe.write";

        /**
         * Autorisation pour consulter le statut d'une demande de paiement en masse
         */
        public static final String DEMANDE_PAIEMENT_GROUPE_READ = "demande_paiement_groupe.read";
    }

    static class Paiement {
        /**
         * Autorisation d'effectuer un paiement
         */
        public static final String PAIEMENT_WRITE = "paiement.write";

        /**
         * Autorisation pour consulter des paiements
         */
        public static final String PAIEMENT_READ = "paiement.read";

        /**
         * Autorisation de créer des paiements en masse
         */
        public static final String PAIEMENT_GROUPE_WRITE = "paiement_groupe.write";

        /**
         * Autorisation pour consulter le statut d'un paiement en masse
         */
        public static final String PAIEMENT_GROUPE_READ = "paiement_groupe.read";
    }

    static class RetourFonds {
        /**
         * Authorization to perform fund returns
         */
        public static final String RETOUR_FONDS_WRITE = "retour_fonds.write";
    }

    static class DemandeAnnulation {
        /**
         * Authorization to send payment cancellation requests
         */
        public static final String DEMANDE_ANNULATION_WRITE = "demande_annulation.write";

        /**
         * Authorization to respond to payment cancellation requests
         */
        public static final String DEMANDE_ANNULATION_REPONSE_WRITE = "demande_annulation_reponse.write";
    }

    /**
     * All scopes related to Compte management
     */
    public static final List<String> COMPTE_ALL = Collections.unmodifiableList(Arrays.asList(
            Compte.COMPTE_READ,
            Compte.COMPTE_TRANSACTION_WRITE,
            Compte.COMPTE_TRANSACTION_READ
    ));

    /**
     * All scopes related to alias management
     */
    public static final List<String> ALIAS_ALL = Collections.unmodifiableList(Arrays.asList(
            Alias.ALIAS_READ,
            Alias.ALIAS_WRITE,
            Alias.ALIAS_DELETE
    ));

    /**
     * All scopes related to webhook management
     */
    public static final List<String> WEBHOOK_ALL = Collections.unmodifiableList(Arrays.asList(
            Webhook.WEBHOOK_READ,
            Webhook.WEBHOOK_WRITE,
            Webhook.WEBHOOK_DELETE
    ));

    /**
     * All scopes used for payment operations (single and bulk)
     */
    public static final List<String> PAIEMENT_ALL = Collections.unmodifiableList(Arrays.asList(
            Paiement.PAIEMENT_READ,
            Paiement.PAIEMENT_WRITE,
            Paiement. PAIEMENT_GROUPE_READ,
            Paiement.PAIEMENT_GROUPE_WRITE
    ));

    /**
     * All scopes used for payment request operations
     */
    public static final List<String> DEMANDE_PAIEMENT_ALL = Collections.unmodifiableList(Arrays.asList(
            DemandePaiement.DEMANDE_PAIEMENT_READ,
            DemandePaiement.DEMANDE_PAIEMENT_WRITE,
            DemandePaiement.DEMANDE_PAIEMENT_REPONSE_WRITE,
            DemandePaiement.DEMANDE_PAIEMENT_GROUPE_READ,
            DemandePaiement.DEMANDE_PAIEMENT_GROUPE_WRITE
    ));


    /**
     * All scopes used for cancel request operations
     */
    public static final List<String> DEMANDE_ANNULATION_ALL = Collections.unmodifiableList(Arrays.asList(
            DemandeAnnulation.DEMANDE_ANNULATION_REPONSE_WRITE,
            DemandeAnnulation.DEMANDE_ANNULATION_WRITE
    ));


    /**
     * All available scopes in the PiSpi API.
     */
    public static final List<String> ALL_SCOPES = Collections.unmodifiableList(Arrays.asList(
            Compte.COMPTE_READ,
            Compte.COMPTE_TRANSACTION_WRITE,
            Compte.COMPTE_TRANSACTION_READ,
            Alias.ALIAS_READ,
            Alias.ALIAS_WRITE,
            Alias.ALIAS_DELETE,
            Webhook.WEBHOOK_READ,
            Webhook.WEBHOOK_WRITE,
            Webhook.WEBHOOK_DELETE,
            DemandePaiement.DEMANDE_PAIEMENT_WRITE,
            DemandePaiement.DEMANDE_PAIEMENT_READ,
            DemandePaiement.DEMANDE_PAIEMENT_REPONSE_WRITE,
            DemandePaiement.DEMANDE_PAIEMENT_GROUPE_WRITE,
            DemandePaiement.DEMANDE_PAIEMENT_GROUPE_READ,
            Paiement.PAIEMENT_WRITE,
            Paiement.PAIEMENT_READ,
            Paiement.PAIEMENT_GROUPE_WRITE,
            Paiement.PAIEMENT_GROUPE_READ,
            RetourFonds.RETOUR_FONDS_WRITE,
            DemandeAnnulation.DEMANDE_ANNULATION_WRITE,
            DemandeAnnulation.DEMANDE_ANNULATION_REPONSE_WRITE
    ));

    public static String scopesToString(List<String> scopes) {
        if (scopes == null || scopes.isEmpty()) {
            return "";
        }
        return String.join(" ", scopes);
    }
}
