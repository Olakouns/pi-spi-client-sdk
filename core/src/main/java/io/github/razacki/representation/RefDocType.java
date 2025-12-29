package io.github.razacki.representation;

import io.github.razacki.exception.PiSpiException;

public enum RefDocType {
    QUOT("Quotation", "Devis commercial détaillant les conditions d'offre."),
    PROF("Proforma Invoice", "Facture pro forma, engagement préliminaire du vendeur (prix/conditions), sans enregistrement comptable réel."),
    CINV("Commercial Invoice", "Facture commerciale standard pour la vente de biens ou services. Utilisée pour les paiements directs d'une facture émise par le vendeur."),
    CMCN("Commercial Contract", "Contrat commercial entre les parties, stipulant les termes et conditions de la livraison de biens ou de services."),
    DISP("Dispatch Advice", "Avis d'expédition ou bon de livraison."),
    PUOR("Purchase Order", "Bon de commande émis par l'acheteur."),
    CONT("Contract", "Document contractuel prouvant un accord entre vendeur et acheteur pour biens ou services."),
    HIRI("Hire Invoice", "Facture pour location de ressources humaines (ex : intérim) ou biens/équipements."),
    INVS("Invoice Signed", "Facture signée, validée électroniquement ou physiquement. Indique une approbation formelle avant paiement"),
    MSIN("Metered Service Invoice", "Facture pour services mesurés (ex : gaz, électricité via compteur fixe). Inclut la consommation réelle."),
    SPRR("Seller Presentment", "Document de présentation par le vendeur pour supporter l'acquisition (ex : preuve de livraison ou justificatif)"),
    TISH("Time Sheet", "Feuille de temps enregistrant les heures pour services/prestations. Pour paiements basés sur le temps (ex : consulting, freelance)."),
    USAR("Usage Report", "Rapport d'utilisation indiquant le pattern de consommation (ex : SaaS, télécoms). Similaire à MSIN mais plus large (non forcément mesuré)."),
    BOLD("Bill Of Lading", " Document de transport maritime ou logistique (connaissement) prouvant l'expédition des marchandises. Spécifique au transport multimodal (ex : maritime, aérien)."),
    SOAC("Statement Of Account", "elevé de compte émis par le fournisseur, listant les transactions enregistrées pour le débiteur (ex : factures, paiements, ajustements)."),
    VCHR("Voucher", "Document électronique de paiement (bon ou coupon) représentant une obligation ou un droit de paiement. Souvent utilisé pour des paiements préautorisés ou des remises spécifiques.");

    private final String label;
    private final String description;

    RefDocType(String label, String description) {
        this.label = label;
        this.description = description;
    }

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }

    public static RefDocType fromString(String value) {
        for (RefDocType type : RefDocType.values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new PiSpiException("Unknown RefDocType: " + value);
    }
}
