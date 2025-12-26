package io.github.representation.enums;

public enum DemandePaiementGroupeStatut {
    /**
     * Créée avec confirmation demandée, en attente de confirmation
     */
    INITIE,

    /**
     * Confirmée par le business.
     * Le traitement est terminé lorsque :
     * transactionsIrrevocables + transactionsRejetees == transactionsTotal
     */
    CONFIRME,

    /**
     * Annulée par le business
     */
    ANNULE
}
