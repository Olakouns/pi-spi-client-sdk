package io.github.razacki.representation.enums;

public enum PaiementAnnulationStatut {
    INITIE("si la demande d'annulation est initiée"),
    ENVOYE("si la demande d'annulation est envoyée"),
    IRREVOCABLE("lorsque le retour de fonds est irrévocable"),
    REJETE("lorsque la demande d'annulation est rejetée");

    private final String description;

    PaiementAnnulationStatut(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }


    public static PaiementAnnulationStatut fromCode(String code) {
        for (PaiementAnnulationStatut reason : PaiementAnnulationStatut.values()) {
            if (reason.name().equalsIgnoreCase(code)) {
                return reason;
            }
        }
        return null;
    }
}
