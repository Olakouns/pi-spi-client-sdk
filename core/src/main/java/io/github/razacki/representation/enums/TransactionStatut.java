package io.github.razacki.representation.enums;

public enum TransactionStatut {
    INITIE("Le transfert est en cours de traitement"),
    IRREVOCABLE("Le tranfert à été effectué avec succès"),
    REJETE("Le tranfert à été rejeté");

    private final String description;

    TransactionStatut(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }


    public static TransactionStatut fromCode(String code) {
        for (TransactionStatut reason : TransactionStatut.values()) {
            if (reason.name().equalsIgnoreCase(code)) {
                return reason;
            }
        }
        return null;
    }
}