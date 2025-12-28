package io.github.representation.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum PaiementCategory {

    CODE_631("631", "Demande de paiement provenant d'un particulier"),
    CODE_000("000", "Paiement par QR Code Statique"),
    CODE_400("400", "Paiement par QR Code Dynamique"),
    CODE_401("401", "Autres demandes de paiement de facture"),
    CODE_733("733", "Ordre de paiement via l'API Business"),
    CODE_300("300", "Via le canal USSD"),
    CODE_999("999", "Ordre de transfert bancaire"),
    CODE_500("500", "Demande de Paiement sur site"),
    CODE_521("521", "Demande de Paiement e-commerce");

    private final String code;
    private final String description;

    PaiementCategory(String code, String description) {
        this.code = code;
        this.description = description;
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    @JsonCreator
    public static PaiementCategory fromJson(String code) {
        return Arrays.stream(values())
                .filter(c -> c.code.equals(code))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("Code categorie inconnu : " + code));
    }
}
