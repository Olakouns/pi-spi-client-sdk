package io.github.representation.enums;


public enum DemandesStatutRaison {
    BE23("Alias invalid", "L'alias du payeur n'existe pas ou est invalide."),
    DU03("Duplicate Transaction", "Le txId n'est pas unique."),
    FF10("Internal System Error", "Erreur de traitement du système interne du participant."),
    AC04("Closed Account Number", "Le compte du client payeur est clôturé."),
    AC06("Blocked Account", "Le compte du client payeur est bloqué."),
    AEXR("Already Expired RTP", "La demande de paiement a déjà expiré."),
    AG03("Transaction Not Supported", "Le client n'est pas autorisé à payer en mode débit différé."),
    AG10("Agent Suspended", "Le participant payeur est suspendu."),
    AG11("Creditor Agent Suspended", "Le participant payé est désactivé."),
    ALAC("Already Accepted RTP", "La demande de paiement a déjà été acceptée."),
    AM02("Not Allowed Amount", "Le montant dépasse le montant maximum autorisé."),
    AM09("Wrong Amount", "Le montant reçu ne correspond pas au montant attendu."),
    AM14("Amount Exceeds Agreed Limit", "Le montant dépasse le plafond de débit différé du client."),
    APAR("Already Paid RTP", "Le paiement demandé a déjà été effectué par le payeur."),
    ARFR("Already Refused RTP", "La demande de paiement a déjà été refusée."),
    ARJR("Already Rejected RTP", "La demande de paiement a déjà été rejetée."),
    BE01("Inconsistent With End Customer", "L'identification du client n'est pas liée au numéro de compte."),
    BE05("Unrecognised Initiating Party", "La partie initiatrice n'est pas reconnue par le client final."),
    FR01("Fraud", "Rejeté pour suspicion de fraude."),
    RR07("Remittance Information Invalid", "Le justificatif de la demande de paiement est invalide.");

    private final String label;
    private final String description;

    DemandesStatutRaison(String label, String description) {
        this.label = label;
        this.description = description;
    }

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }


    public static DemandesStatutRaison fromCode(String code) {
        for (DemandesStatutRaison reason : DemandesStatutRaison.values()) {
            if (reason.name().equalsIgnoreCase(code)) {
                return reason;
            }
        }
        return null;
    }
}