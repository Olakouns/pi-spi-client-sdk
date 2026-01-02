package io.github.olakouns.representation.enums;

public enum PiWebhookEvent {
    PAIEMENT_RECU("Lorsque le business reçoit un paiement ou lorsqu'une demande de paiement est acceptée"),
    PAIEMENT_ENVOYE("Lorsque le business envoie un ordre de paiement et que celui-ci est irrévocable."),
    PAIEMENT_REJETE("Lorsque le business envoie un ordre de paiement et que celui-ci est rejeté."),

    RTP_RECU("Lorsque le business reçoit une demande de paiement"),
    RTP_REJETE("Lorsque le business envoie une demande de paiement et que cette demande est rejetée."),
    RTP_REPONSE_REJETE("Lorsque le business rejette une demande de paiement et que cette réponse de rejet est rejetée."),

    ANNULATION_DEMANDE("Lorsque le business reçoit une demande d'annulation de paiement"),
    ANNULATION_REPONSE_REJETE("Lorsque le business rejette une demande d'annulation de paiement et que cette réponse de rejet est rejetée."),
    RETOUR_ENVOYE("Lorsque le business envoie un retour de fonds et que celui-ci est irrévocable."),
    RETOUR_REJETE("Lorsque le business envoie un retour de fonds et que celui-ci est rejeté."),
    RETOUR_RECU("Lorsque le business reçoit un retour de fonds ou lorsqu'une demande d'annulation de paiement a été acceptée"),
    ANNULATION_REJETE("Lorsque le business envoie une demande d'annulation de paiement et que celle-ci est rejetée.");

    private final String description;

    PiWebhookEvent(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }


    public static PiWebhookEvent fromCode(String code) {
        for (PiWebhookEvent reason : PiWebhookEvent.values()) {
            if (reason.name().equalsIgnoreCase(code)) {
                return reason;
            }
        }
        return null;
    }
}
