package io.github.razacki.representation.enums;

public enum CompteType {
    CACC("Current Account", "Compte courant utilisé pour enregistrer les débits et crédits lorsqu'aucun compte spécifique n'a été désigné."),
    CARD("Card Account", "Compte utilisé pour les paiements par carte de crédit."),
    CASH("Cash Payment", "Compte utilisé pour le paiement en espèces."),
    CHAR("Charges", "Compte utilisé pour les frais s'il diffère du compte de paiement."),
    CISH("Cash Income", "Compte utilisé pour le paiement des revenus s'il diffère du compte courant en espèces."),
    COMM("Commission", "Compte utilisé pour les commissions s'il diffère du compte de paiement."),
    CPAC("Clearing Participant Settlement Account", "Compte utilisé pour enregistrer les écritures de règlement débit et crédit au nom d'un participant de compensation désigné."),
    LLSV("Limited Liquidity Savings Account", "Compte utilisé pour l'épargne avec des conditions spéciales d'intérêt et de retrait."),
    LOAN("Loan", "Compte utilisé pour les prêts."),
    MGLD("Marginal Lending", "Compte utilisé pour une facilité de prêt marginal."),
    MOMA("Money Market", "Compte utilisé pour les marchés monétaires s'il diffère du compte en espèces."),
    NREX("Non Resident External", "Compte utilisé pour les non-résidents externes."),
    ODFT("Overdraft", "Compte utilisé pour les découverts."),
    ONDP("Over Night Deposit", "Compte utilisé pour les dépôts au jour le jour."),
    SACC("Settlement Account", "Compte utilisé pour enregistrer les écritures débit et crédit résultant de transactions compensées et réglées via un système spécifique de compensation et de règlement."),
    SLRY("Salary", "Comptes utilisés pour les paiements de salaires."),
    SVGS("Savings", "Compte utilisé pour l'épargne."),
    TAXE("Tax", "Compte utilisé pour les taxes s'il diffère du compte de paiement."),
    TRAN("Transacting Account", "Compte de transaction, le type de compte bancaire le plus basique. La principale différence entre les comptes de transaction et les comptes chèques est qu'il n'y a généralement pas de chéquier ni de facilité de découvert."),
    TRAS("Cash Trading", "Compte utilisé pour le trading s'il diffère du compte en espèces courant."),
    VACC("Virtual Account", "Compte créé virtuellement pour faciliter la collecte et le rapprochement."),
    OTHR("Other Account", "Type de compte non spécifié");

    private final String label;
    private final String description;

    CompteType(String label, String description) {
        this.label = label;
        this.description = description;
    }

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }


    public static CompteType fromCode(String code) {
        for (CompteType reason : CompteType.values()) {
            if (reason.name().equalsIgnoreCase(code)) {
                return reason;
            }
        }
        return null;
    }

}
