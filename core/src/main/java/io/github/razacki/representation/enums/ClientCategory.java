package io.github.razacki.representation.enums;

public enum ClientCategory {
    P("Particulier (Personne physique)"),
    C("Commerçant ou Entreprise individuelle"),
    B("Entreprise"),
    G("Entité gouvernementale");

    private final String description;

    ClientCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }


    public static ClientCategory fromCode(String code) {
        for (ClientCategory reason : ClientCategory.values()) {
            if (reason.name().equalsIgnoreCase(code)) {
                return reason;
            }
        }
        return null;
    }
}
