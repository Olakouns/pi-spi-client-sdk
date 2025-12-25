package io.github.representation.enums;

public enum DemandesPaiementsStatut {
    INITIE("la confirmation du client est attendue suite à la recherche d'alias"),
    ANNULE("lors de la confirmation, le business a annulé l'envoi du paiement"),
    ENVOYE("les validations sont concluantes et que le PSP a envoyé la demande"),
    IRREVOCABLE("le payeur a accepté la demande"),
    REJETE("le payeur rejette la demande");

    private final String description;

    DemandesPaiementsStatut(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }


    public static DemandesPaiementsStatut fromCode(String code) {
        for (DemandesPaiementsStatut reason : DemandesPaiementsStatut.values()) {
            if (reason.name().equalsIgnoreCase(code)) {
                return reason;
            }
        }
        return null;
    }
}
