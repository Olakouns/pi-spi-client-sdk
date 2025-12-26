package io.github.representation;


import io.github.representation.enums.ClientCategory;

public class EnrollmentRepresentation {
    private Client client;
    private Alias alias;

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Alias getAlias() {
        return alias;
    }

    public void setAlias(Alias alias) {
        this.alias = alias;
    }


    public static class Client {
        private String nom;
        private String pays;
        private ClientCategory categorie;

        public String getNom() {
            return nom;
        }

        public void setNom(String nom) {
            this.nom = nom;
        }

        public String getPays() {
            return pays;
        }

        public void setPays(String pays) {
            this.pays = pays;
        }

        public ClientCategory getCategorie() {
            return categorie;
        }

        public void setCategorie(ClientCategory categorie) {
            this.categorie = categorie;
        }
    }

    public static class Alias {
        private String cle;

        public String getCle() {
            return cle;
        }

        public void setCle(String cle) {
            this.cle = cle;
        }
    }
}
