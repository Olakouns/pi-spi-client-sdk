package io.github.representation.enums;

public enum PaiementsStatut {
    INITIE("la confirmation du client est attendue suite à la recherche d'alias"),
    ANNULE("lors de la confirmation, le business a annulé l'envoi du paiement"),
    ENVOYE("les validations sont concluantes et que le PSP a envoyé la demande"),
    IRREVOCABLE("le payeur a accepté la demande"),
    REJETE("le payeur rejette la demande");

    private final String description;

    PaiementsStatut(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }


    public static PaiementsStatut fromCode(String code) {
        for (PaiementsStatut reason : PaiementsStatut.values()) {
            if (reason.name().equalsIgnoreCase(code)) {
                return reason;
            }
        }
        return null;
    }
}
