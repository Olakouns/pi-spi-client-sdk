package io.github.razacki.resource.wrapper;

import io.github.razacki.resource.ApiResource;

import javax.ws.rs.client.WebTarget;

public class ApiResourceWrapper {
    private final ApiResource proxy;
    private final WebTarget target;

    private ComptesResourceWrapper comptes;
    private WebhookResourceWrapper webhooks;
    private DemandesPaiementsResourceWrapper paiementsResourceWrapper;
    private DemandePaiementGroupeResourceWrapper demandePaiementGroupeResourceWrapper;
    private PaiementResourceWrapper paiementResourceWrapper;
    private PaiementGroupeResourceWrapper paiementGroupeResourceWrapper;
    private EnrollmentResourceWrapper enrollmentResourceWrapper;

    public ApiResourceWrapper(ApiResource proxy, WebTarget target) {
        this.proxy = proxy;
        this.target = target;
    }

    public synchronized ComptesResourceWrapper comptes() {
        if (this.comptes == null) {
            this.comptes = new ComptesResourceWrapper(proxy.comptes(), target.path("/comptes"));
        }
        return this.comptes;
    }

    public synchronized EnrollmentResourceWrapper enrollment() {
        if (this.enrollmentResourceWrapper == null) {
            this.enrollmentResourceWrapper = new EnrollmentResourceWrapper(proxy.enrollment(), target.path("/alias"));
        }
        return this.enrollmentResourceWrapper;
    }



    public synchronized WebhookResourceWrapper webhooks() {
        if (this.webhooks == null) {
            this.webhooks = new WebhookResourceWrapper(proxy.webhooks(), target.path("/webhooks"));
        }
        return this.webhooks;
    }

    public synchronized DemandesPaiementsResourceWrapper demandesPaiements() {
        if (this.paiementsResourceWrapper == null) {
            this.paiementsResourceWrapper = new DemandesPaiementsResourceWrapper(proxy.demandesPaiements(), target.path("/demandes-paiements"));
        }
        return this.paiementsResourceWrapper;
    }

    public synchronized DemandePaiementGroupeResourceWrapper demandesPaiementsGroupes() {
        if (this.demandePaiementGroupeResourceWrapper == null) {
            this.demandePaiementGroupeResourceWrapper = new DemandePaiementGroupeResourceWrapper(proxy.demandesPaiementsGroupes(), target.path("/demandes-paiements-groupes"));
        }
        return this.demandePaiementGroupeResourceWrapper;
    }

    public synchronized PaiementResourceWrapper paiements() {
        if (this.paiementResourceWrapper == null) {
            this.paiementResourceWrapper = new PaiementResourceWrapper(proxy.paiements(), target.path("/paiements"));
        }
        return this.paiementResourceWrapper;
    }

    public synchronized PaiementGroupeResourceWrapper paiementsGroupes() {
        if (this.paiementGroupeResourceWrapper == null) {
            this.paiementGroupeResourceWrapper = new PaiementGroupeResourceWrapper(proxy.paiementsGroupes(), target.path("/paiements-groupes"));
        }
        return this.paiementGroupeResourceWrapper;
    }
}
