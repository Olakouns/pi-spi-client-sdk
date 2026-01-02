package io.github.olakouns.representation.enums;

public enum TransactionStatutRaison {
    SOLDE_INSUFFISANT("le solde du compte debiteur est insuffisant pour effectuer l'opération."),
    COMPTE_BLOQUE("le compte debiteur ou le compte crediteur est bloqué"),
    COMPTE_CLOTURE("le compte debiteur ou le compte crediteur est cloturé");

    private final String description;

    TransactionStatutRaison(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }


    public static TransactionStatutRaison fromCode(String code) {
        for (TransactionStatutRaison reason : TransactionStatutRaison.values()) {
            if (reason.name().equalsIgnoreCase(code)) {
                return reason;
            }
        }
        return null;
    }
}
