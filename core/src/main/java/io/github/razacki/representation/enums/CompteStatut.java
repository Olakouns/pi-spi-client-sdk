package io.github.razacki.representation.enums;

public enum CompteStatut {
    OUVERT("Le compte est actif et opérationnel"),
    BLOQUE("Le compte est temporairement bloqué"),
    CLOTURE("Le compte est fermé définitivement");

    private final String description;

    CompteStatut(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }


    public static CompteStatut fromCode(String code) {
        for (CompteStatut reason : CompteStatut.values()) {
            if (reason.name().equalsIgnoreCase(code)) {
                return reason;
            }
        }
        return null;
    }
}
