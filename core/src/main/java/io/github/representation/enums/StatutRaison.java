package io.github.representation.enums;


public enum StatutRaison {
    BE23("Alias invalid", "L'alias du payeur n'existe pas ou est invalide."),
    DU03("Duplicate Transaction", "Le txId n'est pas unique."),
    FF10("Internal System Error", "Erreur de traitement du système interne du participant."),
    AG07("Insufficient Funds", "Le solde du compte debiteur est insuffisant pour effectuer l'opération."),
    AB03("Aborted Settlement Timeout", "Message en timeout lors du règlement."),
    AB04("Aborted Settlement Fatal Error", "Transaction rejetée à cause d'une erreur fatale."),
    AB08("Offline Creditor Agent", "Le système du participant payé n'est pas accessible."),
    AB09("Error Creditor Agent", "Transaction rejetée à cause d'une erreur chez le participant payé."),
    AC03("Invalid Creditor Account Number", "Le numéro de compte du payé est invalide."),
    AC04("Closed Account Number", "Le compte du client payeur est clôturé."),
    AC06("Blocked Account", "Le compte du client payeur est bloqué."),
    AC07("Closed Creditor Account Number", "Le compte du payé est clôturé."),
    AG01("Transaction Forbidden", "Transaction interdite sur ce type de compte."),
    AG03("Transaction Not Supported", "Le client n'est pas autorisé à payer en mode débit différé."),
    AG10("Agent Suspended", "Le participant payeur est suspendu."),
    AG11("Creditor Agent Suspended", "Le participant payé est désactivé."),
    AEXR("Already Expired RTP", "La demande de paiement a déjà expiré."),
    ALAC("Already Accepted RTP", "La demande de paiement a déjà été acceptée."),
    AM02("Not Allowed Amount", "Le montant de la transaction est supérieur au maximum autorisé"),
    AM04("Insufficient Funds", "Le solde de garantie du participant payeur est insuffisant."),
    AM09("Wrong Amount", "Le montant reçu ne correspond pas au montant attendu."),
    AM21("Limit Exceeded", "Le montant de la transaction dépasse les limites convenues entre le participant et le client."),
    AM14("Amount Exceeds Agreed Limit", "Le montant dépasse le plafond de débit différé du client."),
    APAR("Already Paid RTP", "Le paiement demandé a déjà été effectué par le payeur."),
    ARFR("Already Refused RTP", "La demande de paiement a déjà été refusée."),
    ARJR("Already Rejected RTP", "La demande de paiement a déjà été rejetée."),
    ARDT("Already Returned", "La transaction a déjà été annulée"),
    BE01("Inconsistent With End Customer", "L'identification du client final n'est pas liée au numéro de compte associé."),
    BE05("Unrecognised Initiating Party", "La partie initiatrice n'est pas reconnue par le client final."),
    FR01("Fraud", "Rejeté pour suspicion de fraude."),
    IRNR("Initial RTP Never Received", "La demande de paiement n'a jamais été reçue."),
    RR04("Regulatory Reason", "Raison règlementaire, notamment lorsque le bénéficiaire figure dans une liste d'interdiction."),
    RR07("Remittance Information Invalid", "Le justificatif de la demande de paiement est invalide."),
    CUST("Customer Decision", "Lorsque la demande d'annulation (camt.056) est rejetée par le client payé.");

    private final String label;
    private final String description;

    StatutRaison(String label, String description) {
        this.label = label;
        this.description = description;
    }

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }


    public static StatutRaison fromCode(String code) {
        for (StatutRaison reason : StatutRaison.values()) {
            if (reason.name().equalsIgnoreCase(code)) {
                return reason;
            }
        }
        return null;
    }
}