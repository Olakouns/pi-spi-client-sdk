package io.github.representation.enums;

public enum PaiementAnnulationMotif {
    AC03("Erreur sur le destinataire"),
    AM09("Erreur sur le montant"),
    SVNR("Service non rendu"),
    DUPL("Transaction déjà payée"),
    FRAD("Suspicion de fraude");

    private final String description;

    PaiementAnnulationMotif(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }


    public static PaiementAnnulationMotif fromCode(String code) {
        for (PaiementAnnulationMotif reason : PaiementAnnulationMotif.values()) {
            if (reason.name().equalsIgnoreCase(code)) {
                return reason;
            }
        }
        return null;
    }
}
