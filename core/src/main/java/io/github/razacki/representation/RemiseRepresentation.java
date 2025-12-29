package io.github.razacki.representation;

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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final RemiseRepresentation representation = new RemiseRepresentation();

        public Builder montant(BigDecimal montant) {
            representation.setMontant(montant);
            return this;
        }

        public Builder taux(BigDecimal taux) {
            representation.setTaux(taux);
            return this;
        }

        public RemiseRepresentation build() {
            return representation;
        }
    }
}
