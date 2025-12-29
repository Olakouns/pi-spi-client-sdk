package io.github.razacki.representation.enums;

public enum PaiementRetourStatut {
    INITIE("si l'envoi du retour de fonds est initié"),
    ENVOYE("si le participant a envoyé le retour de fonds"),
    IRREVOCABLE("lorsque le retour de fonds est irrévocable"),
    REJETE("Lorsque le retour de fonds est rejeté");

    private final String description;

    PaiementRetourStatut(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }


    public static PaiementRetourStatut fromCode(String code) {
        for (PaiementRetourStatut reason : PaiementRetourStatut.values()) {
            if (reason.name().equalsIgnoreCase(code)) {
                return reason;
            }
        }
        return null;
    }
}
