package io.github.representation;

import java.math.BigDecimal;

public class RemiseRepresentation {
    private BigDecimal montant;
    private BigDecimal taux;

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public BigDecimal getTaux() {
        return taux;
    }

    public void setTaux(BigDecimal taux) {
        this.taux = taux;
    }
}
